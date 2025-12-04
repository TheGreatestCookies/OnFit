package kspo.onfit.member.service;

import kspo.onfit.member.domain.Member;
import kspo.onfit.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberLowService {

    private final MemberRepository memberRepository;

    public Member getReferenceById(Long id) {
        return memberRepository.getReferenceById(id);
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }
}
