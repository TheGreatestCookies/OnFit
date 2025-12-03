package kspo.onfit.chatbot.service;

import kspo.onfit.chatbot.domain.FitnessMeasure;
import kspo.onfit.chatbot.dto.FitnessPrescriptionDto;
import kspo.onfit.chatbot.repository.FitnessMeasureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FitnessMeasureService {

    private final FitnessMeasureRepository fitnessMeasureRepository;

    public List<FitnessPrescriptionDto> findSimilarPrescriptions(
            int age,
            BigDecimal height,
            BigDecimal weight,
            BigDecimal bodyFatPercentage
    ) {
        int minAge = age - 5;
        int maxAge = age + 5;
        BigDecimal minHeight = height.subtract(BigDecimal.valueOf(10));
        BigDecimal maxHeight = height.add(BigDecimal.valueOf(10));
        BigDecimal minWeight = weight.subtract(BigDecimal.valueOf(10));
        BigDecimal maxWeight = weight.add(BigDecimal.valueOf(10));
        BigDecimal minBodyFat = bodyFatPercentage.subtract(BigDecimal.valueOf(5));
        BigDecimal maxBodyFat = bodyFatPercentage.add(BigDecimal.valueOf(5));

        List<FitnessMeasure> similarData = fitnessMeasureRepository.findSimilarFitnessData(
                minAge, maxAge,
                minHeight, maxHeight,
                minWeight, maxWeight,
                minBodyFat, maxBodyFat
        );

        return similarData.stream()
                .map(data -> FitnessPrescriptionDto.builder()
                        .age(data.getMesureAgeCo())
                        .height(data.getHeight())
                        .weight(data.getWeight())
                        .bodyFatPercentage(data.getBodyFatPercentage())
                        .prescription(data.getMvmPrscrptnCn())
                        .build())
                .collect(Collectors.toList());
    }
}

