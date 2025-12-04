package kspo.onfit.chatbot.dto;

import java.util.List;

public record ChatRequestDto(
        String sessionId,
        String userMessage,
        double lat,
        double lng
) {
    public record MessageDto(
            String role,
            String content,
            String toolCallId,
            List<ToolCallDto> toolCalls
    ) {
        public MessageDto(String role, String content) {
            this(role, content, null, null);
        }

        public static MessageDto user(String content) {
            return new MessageDto("user", content);
        }

        public static MessageDto assistant(String content) {
            return new MessageDto("assistant", content);
        }

        public static MessageDto toolResponse(String toolCallId, String content) {
            return new MessageDto("tool", content, toolCallId, null);
        }

        public static MessageDto assistantToolCall(List<ToolCallDto> toolCalls) {
            return new MessageDto("assistant", null, null, toolCalls);
        }
    }

    public record ToolCallDto(
            String id,
            String type,
            FunctionDto function
    ) {
        public static ToolCallDto of(String id, String type, FunctionDto function) {
            return new ToolCallDto(id, type, function);
        }
    }

    public record FunctionDto(
            String name,
            String arguments
    ) {
        public static FunctionDto of(String name, String arguments) {
            return new FunctionDto(name, arguments);
        }
    }
}
