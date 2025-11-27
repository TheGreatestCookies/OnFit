package kspo.onfit.member.service;

import kspo.onfit.member.domain.Member;
import kspo.onfit.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberLowService {

    private final MemberRepository memberRepository;

    public Member getReferenceById(Long id){
        return memberRepository.getReferenceById(id);
    }


}
