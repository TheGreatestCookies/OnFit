package kspo.onfit.chatbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kspo.onfit.chatbot.client.OpenAiClient;
import kspo.onfit.chatbot.domain.HomeWorkoutRecommendationLog;
import kspo.onfit.chatbot.domain.VoucherRecommendationLog;
import kspo.onfit.chatbot.dto.ChatRequestDto;
import kspo.onfit.chatbot.dto.VoucherInfoDto;
import kspo.onfit.chatbot.repository.HomeWorkoutRecommendationLogRepository;
import kspo.onfit.chatbot.repository.VoucherRecommendationLogRepository;
import kspo.onfit.member.domain.Member;
import kspo.onfit.member.repository.MemberRepository;
import kspo.onfit.voucher.repository.VoucherRepository;
import kspo.onfit.video.domain.FitnessVideo;
import kspo.onfit.video.repository.FitnessVideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiClient openAiClient;
    private final VoucherRepository voucherRepository;
    private final FitnessMeasureService fitnessMeasureService;
    private final FitnessVideoRepository fitnessVideoRepository;
    private final VoucherRecommendationLogRepository voucherRecommendationLogRepository;
    private final HomeWorkoutRecommendationLogRepository homeWorkoutRecommendationLogRepository;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, List<ChatRequestDto.MessageDto>> sessionStore = new ConcurrentHashMap<>();
    private final Map<String, Long> sessionMemberStore = new ConcurrentHashMap<>();
    private static final int MAX_TOOL_CALL_DEPTH = 5;

    public Flux<String> chatStream(ChatRequestDto request) {
        String sessionId = request.sessionId();
        String userMessage = request.userMessage();

        // 로그인한 사용자의 memberId를 세션에 저장
        if (request.memberId() != null) {
            sessionMemberStore.put(sessionId, request.memberId());
        }

        List<ChatRequestDto.MessageDto> messages = sessionStore.computeIfAbsent(
                sessionId, k -> new ArrayList<>());

        messages.add(ChatRequestDto.MessageDto.user(userMessage));

        List<VoucherInfoDto> voucherInfos = getVoucherInfos(request.lat(), request.lng());

        return processChatWithChaining(sessionId, messages, voucherInfos, 0);
    }

    private Flux<String> processChatWithChaining(String sessionId,
            List<ChatRequestDto.MessageDto> messages,
            List<VoucherInfoDto> voucherInfos, int depth) {

        if (depth >= MAX_TOOL_CALL_DEPTH) {
            return Flux.empty();
        }

        StreamState state = new StreamState();

        return openAiClient.streamChatResponse(messages, voucherInfos)
                .scan(state, (s, chunk) -> {
                    if (chunk.startsWith("__TOOL_CALL__:")) {
                        s.isToolCall = true;
                        s.toolCallChunks.add(chunk.substring("__TOOL_CALL__:".length()));
                        return s;
                    }

                    if (chunk.equals("__TOOL_CALL_END__")) {
                        s.toolCallEnded = true;
                        return s;
                    }

                    if (!s.isToolCall) {
                        s.contentChunks.add(chunk);
                        s.lastChunk = chunk;
                    }
                    return s;
                })
                .skip(1)
                .flatMap(s -> {
                    try {
                        if (s.isToolCall && s.toolCallEnded && !s.toolCallProcessed) {
                            s.toolCallProcessed = true;
                            return processToolCallWithChaining(sessionId, s, messages, voucherInfos, depth);
                        }

                        if (s.isToolCall) {
                            return Flux.empty();
                        }

                        if (s.lastChunk != null && !s.lastChunk.isEmpty()) {
                            Map<String, Object> chunkMsg = Map.of("type", "talk", "chunk", s.lastChunk);
                            s.lastChunk = "";
                            return Flux.just(objectMapper.writeValueAsString(chunkMsg));
                        }
                        return Flux.empty();
                    } catch (Exception e) {
                        log.error("스트림 처리 실패", e);
                        return Flux.empty();
                    }
                })
                .doOnComplete(() -> {
                    if (!state.isToolCall && !state.contentChunks.isEmpty()) {
                        String fullContent = String.join("", state.contentChunks);
                        if (!fullContent.isBlank()) {
                            messages.add(ChatRequestDto.MessageDto.assistant(fullContent));
                        }
                    }
                });
    }

    private Flux<String> processToolCallWithChaining(String sessionId, StreamState state,
            List<ChatRequestDto.MessageDto> messages,
            List<VoucherInfoDto> voucherInfos, int depth) {
        try {
            List<ToolCallInfo> toolCalls = parseToolCalls(state.toolCallChunks);

            if (toolCalls.isEmpty()) {
                return Flux.empty();
            }

            List<Flux<String>> responseFluxes = new ArrayList<>();

            List<ChatRequestDto.ToolCallDto> toolCallDtos = toolCalls.stream()
                    .map(tc -> ChatRequestDto.ToolCallDto.of(
                            tc.id,
                            "function",
                            ChatRequestDto.FunctionDto.of(tc.name, tc.arguments)
                    ))
                    .collect(Collectors.toList());
            messages.add(ChatRequestDto.MessageDto.assistantToolCall(toolCallDtos));

            for (ToolCallInfo toolCall : toolCalls) {
                ToolCallResult result = executeToolCall(toolCall, voucherInfos, sessionId);

                messages.add(ChatRequestDto.MessageDto.toolResponse(toolCall.id, result.toolResponse));

                if (result.immediateResponse != null) {
                    responseFluxes.add(Flux.just(result.immediateResponse));
                }
            }

            boolean hasOnlyFinalFunctions = toolCalls.stream().allMatch(tc ->
                    "recommend_home_workout".equals(tc.name));

            if (hasOnlyFinalFunctions) {
                return Flux.concat(responseFluxes.toArray(new Flux[0]));
            }

            Flux<String> immediateResponses = Flux.concat(responseFluxes.toArray(new Flux[0]));
            Flux<String> chainedResponse = processChatWithChaining(sessionId, messages, voucherInfos, depth + 1);

            return Flux.concat(immediateResponses, chainedResponse);

        } catch (Exception e) {
            log.error("Tool call 처리 중 오류 발생", e);
            return Flux.empty();
        }
    }

    private List<ToolCallInfo> parseToolCalls(List<String> toolCallChunks) {
        Map<Integer, ToolCallInfo> toolCallMap = new HashMap<>();

        for (String chunk : toolCallChunks) {
            Map<String, Object> parsed = openAiClient.parseToolCall(chunk);
            if (parsed != null) {
                int index = parsed.containsKey("index") ? (Integer) parsed.get("index") : 0;
                ToolCallInfo info = toolCallMap.computeIfAbsent(index, k -> new ToolCallInfo());

                if (parsed.containsKey("id")) info.id = (String) parsed.get("id");
                if (parsed.containsKey("name")) info.name = (info.name != null ? info.name : "") + parsed.get("name");
                if (parsed.containsKey("arguments")) info.arguments = (info.arguments != null ? info.arguments : "") + parsed.get("arguments");
            }
        }

        for (ToolCallInfo info : toolCallMap.values()) {
            if (info.name == null || info.name.isBlank()) {
                info.name = inferFunctionName(info.arguments);
            }
        }

        return new ArrayList<>(toolCallMap.values());
    }

    private String inferFunctionName(String argsJson) {
        if (argsJson == null || argsJson.isBlank()) return "get_fitness_prescription";
        if (argsJson.contains("voucher_numbers")) return "recommend_voucher_facilities";
        if (argsJson.contains("main_exercises") || argsJson.contains("warmup_exercises") || argsJson.contains("cool_down_exercises"))
            return "recommend_home_workout";
        return "get_fitness_prescription";
    }

    private ToolCallResult executeToolCall(ToolCallInfo toolCall, List<VoucherInfoDto> voucherInfos, String sessionId) {
        ToolCallResult result = new ToolCallResult();

        try {
            if (toolCall.arguments == null || toolCall.arguments.isBlank()) {
                result.toolResponse = "Error: Empty arguments";
                return result;
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> args = objectMapper.readValue(toolCall.arguments, Map.class);

            switch (toolCall.name) {
                case "recommend_voucher_facilities":
                    return executeRecommendVoucherFacilities(toolCall.id, args, voucherInfos, sessionId);
                case "recommend_home_workout":
                    return executeRecommendHomeWorkout(toolCall.id, args, sessionId);
                case "get_fitness_prescription":
                    return executeGetFitnessPrescription(toolCall.id, args);
                default:
                    result.toolResponse = "Error: Unknown function";
                    return result;
            }
        } catch (Exception e) {
            log.error("Tool call 실행 중 오류: {}", toolCall.name, e);
            result.toolResponse = "Error: " + e.getMessage();
            return result;
        }
    }

    private ToolCallResult executeRecommendVoucherFacilities(String toolCallId,
            Map<String, Object> args, List<VoucherInfoDto> voucherInfos, String sessionId) throws Exception {
        ToolCallResult result = new ToolCallResult();

        String message = (String) args.get("message");
        @SuppressWarnings("unchecked")
        List<Integer> voucherNumbers = (List<Integer>) args.get("voucher_numbers");
        @SuppressWarnings("unchecked")
        List<String> moodTags = args.get("mood_tags") != null
                ? ((List<String>) args.get("mood_tags")).stream().limit(4).collect(Collectors.toList())
                : Collections.emptyList();

        if (voucherNumbers == null || voucherNumbers.isEmpty()) {
            result.toolResponse = "추천할 시설 번호가 없습니다.";
            return result;
        }

        List<Long> targetIds = voucherNumbers.stream().map(Number::longValue).collect(Collectors.toList());
        List<VoucherInfoDto> recommendedVouchers = voucherInfos.stream()
                .filter(v -> targetIds.contains(v.id()))
                .collect(Collectors.toList());

        saveVoucherRecommendationLog(sessionId, recommendedVouchers, moodTags);

        result.immediateResponse = objectMapper.writeValueAsString(Map.of(
                "type", "recommend",
                "message", message != null ? message : "추천 운동 시설입니다.",
                "vouchers", recommendedVouchers));

        result.toolResponse = String.format(
                "추천 완료: %d개 시설. 이제 '집에서 할 수 있는 간단한 운동도 추천해줄까?'라고 물어보세요. / 위 추천 시설에 대한 설명은 이미 진행했으므로 생략하세요.",
                recommendedVouchers.size());

        return result;
    }

    private ToolCallResult executeRecommendHomeWorkout(String toolCallId,
            Map<String, Object> args, String sessionId) throws Exception {
        ToolCallResult result = new ToolCallResult();

        String message = (String) args.get("message");
        @SuppressWarnings("unchecked")
        List<String> warmupExercises = args.get("warmup_exercises") != null
                ? (List<String>) args.get("warmup_exercises") : Collections.emptyList();
        @SuppressWarnings("unchecked")
        List<String> mainExercises = args.get("main_exercises") != null
                ? (List<String>) args.get("main_exercises") : Collections.emptyList();
        @SuppressWarnings("unchecked")
        List<String> coolDownExercises = args.get("cool_down_exercises") != null
                ? (List<String>) args.get("cool_down_exercises") : Collections.emptyList();
        @SuppressWarnings("unchecked")
        List<String> moodTags = args.get("mood_tags") != null
                ? ((List<String>) args.get("mood_tags")).stream().limit(4).collect(Collectors.toList())
                : Collections.emptyList();

        List<FitnessVideo> warmupVideos = findVideos(warmupExercises);
        List<FitnessVideo> mainVideos = findVideos(mainExercises);
        List<FitnessVideo> coolDownVideos = findVideos(coolDownExercises);

        List<FitnessVideo> allVideos = new ArrayList<>();
        allVideos.addAll(warmupVideos);
        allVideos.addAll(mainVideos);
        allVideos.addAll(coolDownVideos);

        saveHomeWorkoutRecommendationLog(sessionId, warmupExercises, mainExercises, coolDownExercises,
                warmupVideos, mainVideos, coolDownVideos, moodTags);

        result.immediateResponse = objectMapper.writeValueAsString(Map.of(
                "type", "home_workout",
                "message", message != null ? message : "집에서 할 수 있는 맞춤 운동을 추천해드릴게요!",
                "videos", allVideos));

        result.toolResponse = String.format("집 운동 영상 %d개 제공됨.", allVideos.size());
        return result;
    }

    private ToolCallResult executeGetFitnessPrescription(String toolCallId, Map<String, Object> args) {
        ToolCallResult result = new ToolCallResult();

        int age = args.get("age") != null ? ((Number) args.get("age")).intValue() : 30;
        double height = args.get("height") != null ? ((Number) args.get("height")).doubleValue() : 170.0;
        double weight = args.get("weight") != null ? ((Number) args.get("weight")).doubleValue() : 70.0;
        double bodyFat = args.get("body_fat_percentage") != null
                ? ((Number) args.get("body_fat_percentage")).doubleValue() : 20.0;

        var prescriptions = fitnessMeasureService.findSimilarPrescriptions(
                age, java.math.BigDecimal.valueOf(height),
                java.math.BigDecimal.valueOf(weight), java.math.BigDecimal.valueOf(bodyFat));

        StringBuilder text = new StringBuilder("운동처방:\n\n");
        for (int i = 0; i < Math.min(prescriptions.size(), 3); i++) {
            text.append(String.format("%d. %s\n\n", i + 1, prescriptions.get(i).prescription()));
        }

        result.toolResponse = text +
                "\n⚠️ 위 운동을 친근하게 설명하고, recommend_home_workout 함수를 호출하세요.\n" +
                "- warmup_exercises, main_exercises, cool_down_exercises 배열에 운동명을 넣어주세요.";

        return result;
    }

    private List<FitnessVideo> findVideos(List<String> keywords) {
        if (keywords == null) return Collections.emptyList();
        return keywords.stream()
                .map(keyword -> fitnessVideoRepository.findFirstByTitle(keyword).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void clearSession(String sessionId) {
        sessionStore.remove(sessionId);
        sessionMemberStore.remove(sessionId);
    }

    private void saveVoucherRecommendationLog(String sessionId, List<VoucherInfoDto> vouchers, List<String> moodTags) {
        try {
            Long memberId = sessionMemberStore.get(sessionId);
            if (memberId == null) {
                log.debug("비로그인 사용자 - 시설 추천 로그 저장 생략");
                return;
            }

            Member member = memberRepository.findById(memberId).orElse(null);
            if (member == null) {
                return;
            }

            String moodTagsString = moodTags != null && !moodTags.isEmpty()
                    ? String.join(",", moodTags)
                    : null;

            String voucherDetailsJson = objectMapper.writeValueAsString(vouchers);

            VoucherRecommendationLog logEntry = VoucherRecommendationLog.builder()
                    .member(member)
                    .sessionId(sessionId)
                    .voucherDetailsJson(voucherDetailsJson)
                    .moodTags(moodTagsString)
                    .build();

            voucherRecommendationLogRepository.save(logEntry);
            log.info("시설 추천 로그 저장 완료: memberId={}, moodTags={}", memberId, moodTagsString);
        } catch (Exception e) {
            log.error("시설 추천 로그 저장 실패", e);
        }
    }

    private void saveHomeWorkoutRecommendationLog(String sessionId,
            List<String> warmupExercises, List<String> mainExercises, List<String> coolDownExercises,
            List<FitnessVideo> warmupVideos, List<FitnessVideo> mainVideos, List<FitnessVideo> coolDownVideos,
            List<String> moodTags) {
        try {
            Long memberId = sessionMemberStore.get(sessionId);
            if (memberId == null) {
                log.debug("비로그인 사용자 - 집 운동 추천 로그 저장 생략");
                return;
            }

            Member member = memberRepository.findById(memberId).orElse(null);
            if (member == null) {
                return;
            }

            String moodTagsString = moodTags != null && !moodTags.isEmpty()
                    ? String.join(",", moodTags)
                    : null;

            String warmupExercisesString = warmupExercises != null && !warmupExercises.isEmpty()
                    ? String.join(",", warmupExercises)
                    : null;
            String mainExercisesString = mainExercises != null && !mainExercises.isEmpty()
                    ? String.join(",", mainExercises)
                    : null;
            String coolDownExercisesString = coolDownExercises != null && !coolDownExercises.isEmpty()
                    ? String.join(",", coolDownExercises)
                    : null;

            String warmupVideosJson = objectMapper.writeValueAsString(
                    warmupVideos.stream().map(v -> Map.of("title", v.getTitle(), "youtubeCode", v.getYoutubeCode())).collect(Collectors.toList()));
            String mainVideosJson = objectMapper.writeValueAsString(
                    mainVideos.stream().map(v -> Map.of("title", v.getTitle(), "youtubeCode", v.getYoutubeCode())).collect(Collectors.toList()));
            String coolDownVideosJson = objectMapper.writeValueAsString(
                    coolDownVideos.stream().map(v -> Map.of("title", v.getTitle(), "youtubeCode", v.getYoutubeCode())).collect(Collectors.toList()));

            HomeWorkoutRecommendationLog logEntry = HomeWorkoutRecommendationLog.builder()
                    .member(member)
                    .sessionId(sessionId)
                    .warmupExercises(warmupExercisesString)
                    .mainExercises(mainExercisesString)
                    .coolDownExercises(coolDownExercisesString)
                    .warmupVideosJson(warmupVideosJson)
                    .mainVideosJson(mainVideosJson)
                    .coolDownVideosJson(coolDownVideosJson)
                    .moodTags(moodTagsString)
                    .build();

            homeWorkoutRecommendationLogRepository.save(logEntry);
            log.info("집 운동 추천 로그 저장 완료: memberId={}, moodTags={}", memberId, moodTagsString);
        } catch (Exception e) {
            log.error("집 운동 추천 로그 저장 실패", e);
        }
    }

    private List<VoucherInfoDto> getVoucherInfos(double lat, double lng) {
        List<Object[]> results = voucherRepository.findNearestVouchersWithDistance(lat, lng);

        return results.stream()
                .<VoucherInfoDto>map(row -> VoucherInfoDto.of(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        (String) row[2] + " - " + row[3],
                        (String) row[4],
                        row[5] != null ? ((Number) row[5]).intValue() : null,
                        (String) row[6],
                        (String) row[2],
                        row[7] != null ? ((Number) row[7]).doubleValue() : null
                ))
                .collect(Collectors.toList());
    }

    private static class ToolCallInfo {
        String id, name, arguments;
    }

    private static class ToolCallResult {
        String toolResponse, immediateResponse;
    }

    private static class StreamState {
        String lastChunk = "";
        List<String> contentChunks = new ArrayList<>();
        boolean isToolCall = false, toolCallEnded = false, toolCallProcessed = false;
        List<String> toolCallChunks = new ArrayList<>();
    }
}
