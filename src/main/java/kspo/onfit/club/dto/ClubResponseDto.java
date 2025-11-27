package kspo.onfit.club.dto;

import java.time.LocalDate;
import kspo.onfit.club.domain.Club;

public record ClubResponseDto(
        Long id,
        String name,
        String area,
        String addr,
        String sports,
        String category,
        String ageGroup,
        String sex,
        int memberCount,
        LocalDate foundDate
)
{
    public ClubResponseDto(Club club){
        this(
                club.getId(),
                club.getName(),
                club.getArea(),
                club.getAddr(),
                club.getSports(),
                club.getCategory(),
                club.getAgeGroup(),
                club.getSex(),
                club.getMemberCount(),
                club.getFoundDate()
        );
    }
}
