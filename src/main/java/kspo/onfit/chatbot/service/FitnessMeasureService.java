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
        List<FitnessMeasure> similarData = fitnessMeasureRepository.findMostSimilarByBodyType(
                age, height, weight, bodyFatPercentage
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

