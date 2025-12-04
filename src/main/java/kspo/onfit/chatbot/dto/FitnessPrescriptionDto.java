package kspo.onfit.chatbot.dto;

import java.math.BigDecimal;

public record FitnessPrescriptionDto(
        Integer age,
        BigDecimal height,
        BigDecimal weight,
        BigDecimal bodyFatPercentage,
        String prescription
) {
    public static FitnessPrescriptionDto of(
            Integer age,
            BigDecimal height,
            BigDecimal weight,
            BigDecimal bodyFatPercentage,
            String prescription
    ) {
        return new FitnessPrescriptionDto(age, height, weight, bodyFatPercentage, prescription);
    }
}
