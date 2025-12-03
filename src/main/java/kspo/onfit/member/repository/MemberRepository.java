package kspo.onfit.member.repository;

import kspo.onfit.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Override
    Member getReferenceById(Long id);

}
