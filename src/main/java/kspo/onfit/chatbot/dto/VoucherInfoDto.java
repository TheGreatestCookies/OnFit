package kspo.onfit.chatbot.dto;

public record VoucherInfoDto(
        Long id,
        String name,
        String description,
        String category,
        Integer price,
        String telephone,
        String facilityName,
        Double distance,
        Long likeCount
) {
    public static VoucherInfoDto of(
            Long id,
            String name,
            String description,
            String category,
            Integer price,
            String telephone,
            String facilityName,
            Double distance,
            Long likeCount
    ) {
        return new VoucherInfoDto(id, name, description, category, price, telephone, facilityName, distance, likeCount);
    }
}
