package kspo.onfit.chatbot.repository;

import kspo.onfit.chatbot.domain.FitnessMeasure;
import kspo.onfit.chatbot.domain.FitnessMeasureId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface FitnessMeasureRepository extends JpaRepository<FitnessMeasure, FitnessMeasureId> {

    @Query(value = """
        SELECT * FROM fitness_measure
        WHERE MESURE_AGE_CO BETWEEN :minAge AND :maxAge
          AND MESURE_IEM_001_VALUE BETWEEN :minHeight AND :maxHeight
          AND MESURE_IEM_002_VALUE BETWEEN :minWeight AND :maxWeight
          AND MESURE_IEM_003_VALUE BETWEEN :minBodyFat AND :maxBodyFat
          AND MVM_PRSCRPTN_CN IS NOT NULL
          AND MVM_PRSCRPTN_CN != ''
        ORDER BY MESURE_DE DESC
        LIMIT 10
        """, nativeQuery = true)
    List<FitnessMeasure> findSimilarFitnessData(
            @Param("minAge") int minAge,
            @Param("maxAge") int maxAge,
            @Param("minHeight") BigDecimal minHeight,
            @Param("maxHeight") BigDecimal maxHeight,
            @Param("minWeight") BigDecimal minWeight,
            @Param("maxWeight") BigDecimal maxWeight,
            @Param("minBodyFat") BigDecimal minBodyFat,
            @Param("maxBodyFat") BigDecimal maxBodyFat
    );
}

