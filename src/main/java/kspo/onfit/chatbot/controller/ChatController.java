package kspo.onfit.chatbot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kspo.onfit.chatbot.dto.ChatRequestDto;
import kspo.onfit.chatbot.service.ChatService;
import kspo.onfit.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "Chat", description = "챗봇 API")
public class ChatController {

    private final ChatService chatService;

    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "챗봇 메시지 전송 (스트리밍)", description = "세션 기반 대화. sessionId + userMessage만 전송")
    public Flux<ServerSentEvent<String>> chatStream(
            @RequestBody ChatRequestDto request,
            @SessionAttribute(name = "loginMember", required = false) Member loginMember) {
        
        Long memberId = loginMember != null ? loginMember.getId() : null;
        ChatRequestDto requestWithMember = new ChatRequestDto(
                request.sessionId(),
                request.userMessage(),
                request.lat(),
                request.lng(),
                memberId
        );
        
        return chatService.chatStream(requestWithMember)
                .map(json -> ServerSentEvent.<String>builder()
                        .data(json)
                        .build());
    }

    @DeleteMapping("/{sessionId}")
    @Operation(summary = "대화 초기화", description = "해당 세션의 대화 기록을 초기화합니다.")
    public void clearSession(@PathVariable String sessionId) {
        chatService.clearSession(sessionId);
    }
}
