package kspo.onfit.chatbot.dto;

public record VoucherInfoDto(
        Long id,
        String name,
        String description,
        String category,
        Integer price,
        String telephone,
        String facilityName,
        Double distance
) {
    public static VoucherInfoDto of(
            Long id,
            String name,
            String description,
            String category,
            Integer price,
            String telephone,
            String facilityName,
            Double distance
    ) {
        return new VoucherInfoDto(id, name, description, category, price, telephone, facilityName, distance);
    }
}
