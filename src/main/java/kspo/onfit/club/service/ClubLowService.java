package kspo.onfit.club.service;

import kspo.onfit.club.domain.Club;
import kspo.onfit.club.repository.QClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubLowService {

    private final QClubRepository qClubRepository;

    public Page<Club> findClubByAreaAndSports(String area, String sports, Pageable pageable){
        return qClubRepository.findClubByAreaAndSports(area, sports, pageable);
    }

}
