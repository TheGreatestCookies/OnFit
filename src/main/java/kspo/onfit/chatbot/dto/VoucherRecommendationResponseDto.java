package kspo.onfit.chatbot.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kspo.onfit.chatbot.domain.VoucherRecommendationLog;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
public class VoucherRecommendationResponseDto {
    private Long id;
    private List<VoucherInfoDto> vouchers;
    private String moodTags;
    private LocalDateTime createdAt;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static VoucherRecommendationResponseDto from(VoucherRecommendationLog log) {
        List<VoucherInfoDto> vouchers = Collections.emptyList();
        try {
            if (log.getVoucherDetailsJson() != null && !log.getVoucherDetailsJson().isEmpty()) {
                vouchers = objectMapper.readValue(log.getVoucherDetailsJson(), 
                        new TypeReference<List<VoucherInfoDto>>() {});
            }
        } catch (Exception e) {
            // JSON 파싱 실패 시 빈 리스트
        }

        return VoucherRecommendationResponseDto.builder()
                .id(log.getId())
                .vouchers(vouchers)
                .moodTags(log.getMoodTags())
                .createdAt(log.getCreatedAt())
                .build();
    }
}

