package kspo.onfit.member.repository;

import java.util.Optional;
import kspo.onfit.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    
    Optional<Member> findMemberById(Long id);

    Member getReferenceById(Long aLong);
}
