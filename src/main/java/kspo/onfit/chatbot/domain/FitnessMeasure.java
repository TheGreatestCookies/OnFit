package kspo.onfit.chatbot.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Getter
@IdClass(FitnessMeasureId.class)
@Table(name = "fitness_measure")
public class FitnessMeasure {

    @Id
    @Column(name = "MBER_SEQ_NO_VALUE", length = 32)
    private String mberSeqNoValue;

    @Id
    @Column(name = "MESURE_SEQ_NO", length = 20)
    private String mesureSeqNo;

    @Column(name = "CNTER_NM", length = 100)
    private String cnterNm;

    @Column(name = "AGRDE_FLAG_NM", length = 50)
    private String agrdeFlagNm;

    @Column(name = "MESURE_PLACE_FLAG_NM", length = 50)
    private String mesurePlaceFlagNm;

    @Column(name = "MESURE_AGE_CO")
    private Integer mesureAgeCo;

    @Column(name = "INPT_FLAG_NM", length = 30)
    private String inptFlagNm;

    @Column(name = "CRTFC_FLAG_NM", length = 30)
    private String crtfcFlagNm;

    @Column(name = "MESURE_DE", length = 8, nullable = false)
    private String mesureDe;

    @Column(name = "SEXDSTN_FLAG_CD", length = 1)
    private String sexdstnFlagCd;

    @Column(name = "MESURE_IEM_001_VALUE", precision = 5, scale = 2)
    private BigDecimal height;

    @Column(name = "MESURE_IEM_002_VALUE", precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(name = "MESURE_IEM_003_VALUE", precision = 5, scale = 2)
    private BigDecimal bodyFatPercentage;

    @Column(name = "MESURE_IEM_018_VALUE", precision = 5, scale = 2)
    private BigDecimal bmi;

    @Column(name = "MVM_PRSCRPTN_CN", columnDefinition = "TEXT")
    private String mvmPrscrptnCn;
}

