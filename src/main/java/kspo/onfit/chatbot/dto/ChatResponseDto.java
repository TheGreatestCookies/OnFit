package kspo.onfit.chatbot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatResponseDto(
        String type,
        String content,
        String message,
        List<VoucherInfoDto> vouchers
) {
    public static ChatResponseDto talk(String chunk) {
        return new ChatResponseDto("talk", chunk, null, null);
    }

    public static ChatResponseDto recommend(String message, List<VoucherInfoDto> vouchers) {
        return new ChatResponseDto("recommend", null, message, vouchers);
    }

    public static ChatResponseDto homeWorkout(String message) {
        return new ChatResponseDto("home_workout", null, message, null);
    }
}
