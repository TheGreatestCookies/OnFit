package kspo.onfit.club.service;

import kspo.onfit.club.dto.ClubResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubLowService clubLowService;

    public Page<ClubResponseDto> findClubByArea(String area, String sports, Pageable pageable){
        return clubLowService.findClubByAreaAndSports(area, sports, pageable)
                .map(club -> new ClubResponseDto(club));
    }

}
