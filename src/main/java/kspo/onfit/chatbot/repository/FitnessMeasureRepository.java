package kspo.onfit.chatbot.repository;

import kspo.onfit.chatbot.domain.FitnessMeasure;
import kspo.onfit.chatbot.domain.FitnessMeasureId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface FitnessMeasureRepository extends JpaRepository<FitnessMeasure, FitnessMeasureId> {

    /**
     * 체형 유사도 순으로 10개 조회 (유클리드 거리 기반, 정규화 적용)
     */
    @Query(value = """
        SELECT * FROM fitness_measure
        WHERE MVM_PRSCRPTN_CN IS NOT NULL
          AND MVM_PRSCRPTN_CN != ''
          AND MESURE_IEM_001_VALUE IS NOT NULL
          AND MESURE_IEM_002_VALUE IS NOT NULL
        ORDER BY (
            POW((:age - MESURE_AGE_CO) / 10.0, 2) +
            POW((:height - MESURE_IEM_001_VALUE) / 20.0, 2) +
            POW((:weight - MESURE_IEM_002_VALUE) / 20.0, 2) +
            POW((:bodyFat - COALESCE(MESURE_IEM_003_VALUE, :bodyFat)) / 10.0, 2)
        ) ASC
        LIMIT 10
        """, nativeQuery = true)
    List<FitnessMeasure> findMostSimilarByBodyType(
            @Param("age") int age,
            @Param("height") BigDecimal height,
            @Param("weight") BigDecimal weight,
            @Param("bodyFat") BigDecimal bodyFat
    );
}

