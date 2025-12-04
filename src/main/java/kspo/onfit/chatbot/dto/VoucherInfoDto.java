package kspo.onfit.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherInfoDto {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Integer price;
    private String telephone;
    private String facilityName;
    private Double lat;
    private Double lng;
    private Double distance;
}
