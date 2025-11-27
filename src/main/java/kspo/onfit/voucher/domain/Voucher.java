package kspo.onfit.voucher.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String area;

    private Integer areaCode;

    private String facilityName;

    private String sports;

    private Integer sportsCode;

    private Integer sigunguCode;

    private String sigunguName;

    private String addr1;

    private String addr2;

    private String zipCode;

    private String telephone;

    private Integer memberCount;

    private Integer price;
}
