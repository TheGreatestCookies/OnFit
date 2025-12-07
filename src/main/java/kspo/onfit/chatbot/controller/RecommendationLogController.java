package kspo.onfit.chatbot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kspo.onfit.chatbot.domain.HomeWorkoutRecommendationLog;
import kspo.onfit.chatbot.domain.VoucherRecommendationLog;
import kspo.onfit.chatbot.dto.HomeWorkoutRecommendationResponseDto;
import kspo.onfit.chatbot.dto.VoucherRecommendationResponseDto;
import kspo.onfit.chatbot.dto.VoucherInfoDto;
import kspo.onfit.chatbot.repository.HomeWorkoutRecommendationLogRepository;
import kspo.onfit.chatbot.repository.VoucherRecommendationLogRepository;
import kspo.onfit.like.voucherlike.service.VoucherLikeLowService;
import kspo.onfit.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
@Tag(name = "Recommendation", description = "추천 기록 API")
public class RecommendationLogController {

    private final VoucherRecommendationLogRepository voucherRecommendationLogRepository;
    private final HomeWorkoutRecommendationLogRepository homeWorkoutRecommendationLogRepository;
    private final VoucherLikeLowService voucherLikeLowService;

    @GetMapping("/vouchers/my")
    @Operation(summary = "내 시설 추천 기록 조회", description = "로그인한 사용자의 시설 추천 기록을 조회합니다.")
    public ResponseEntity<List<VoucherRecommendationResponseDto>> getMyVoucherRecommendations(
            @SessionAttribute(name = "loginMember", required = false) Member loginMember) {
        if (loginMember == null) {
            return ResponseEntity.ok(List.of());
        }
        List<VoucherRecommendationLog> logs = voucherRecommendationLogRepository.findByMemberIdOrderByCreatedAtDesc(loginMember.getId());
        List<VoucherRecommendationResponseDto> response = logs.stream()
                .map(log -> {
                    VoucherRecommendationResponseDto dto = VoucherRecommendationResponseDto.from(log);
                    // 각 바우처의 최신 좋아요 수 및 내 좋아요 여부 조회 및 업데이트
                    List<VoucherInfoDto> updatedVouchers = dto.getVouchers().stream()
                            .map(voucher -> VoucherInfoDto.of(
                                    voucher.id(),
                                    voucher.name(),
                                    voucher.description(),
                                    voucher.category(),
                                    voucher.price(),
                                    voucher.telephone(),
                                    voucher.facilityName(),
                                    voucher.distance(),
                                    voucherLikeLowService.countVoucherLikeByVoucherId(voucher.id()),
                                    voucherLikeLowService.existsVoucherLikeByVoucherIdAndMemberId(voucher.id(), loginMember.getId())
                            ))
                            .collect(Collectors.toList());
                    return VoucherRecommendationResponseDto.builder()
                            .id(dto.getId())
                            .vouchers(updatedVouchers)
                            .moodTags(dto.getMoodTags())
                            .createdAt(dto.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/home-workouts/my")
    @Operation(summary = "내 집 운동 추천 기록 조회", description = "로그인한 사용자의 집 운동 추천 기록을 조회합니다.")
    public ResponseEntity<List<HomeWorkoutRecommendationResponseDto>> getMyHomeWorkoutRecommendations(
            @SessionAttribute(name = "loginMember", required = false) Member loginMember) {
        if (loginMember == null) {
            return ResponseEntity.ok(List.of());
        }
        List<HomeWorkoutRecommendationLog> logs = homeWorkoutRecommendationLogRepository.findByMemberIdOrderByCreatedAtDesc(loginMember.getId());
        List<HomeWorkoutRecommendationResponseDto> response = logs.stream()
                .map(HomeWorkoutRecommendationResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
