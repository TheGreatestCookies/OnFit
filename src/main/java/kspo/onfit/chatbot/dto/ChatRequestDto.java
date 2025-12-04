package kspo.onfit.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestDto {
    private String sessionId;
    private String userMessage;
    private double lat;
    private double lng;
    private Long memberId;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageDto {
        private String role;
        private String content;
        private String toolCallId;
        private List<ToolCallDto> toolCalls;

        public MessageDto(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public static MessageDto user(String content) {
            return new MessageDto("user", content);
        }

        public static MessageDto assistant(String content) {
            return new MessageDto("assistant", content);
        }

        public static MessageDto toolResponse(String toolCallId, String content) {
            return MessageDto.builder()
                    .role("tool")
                    .content(content)
                    .toolCallId(toolCallId)
                    .build();
        }

        public static MessageDto assistantToolCall(List<ToolCallDto> toolCalls) {
            return MessageDto.builder()
                    .role("assistant")
                    .content(null)
                    .toolCalls(toolCalls)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ToolCallDto {
        private String id;
        private String type;
        private FunctionDto function;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FunctionDto {
        private String name;
        private String arguments;
    }
}
