package kspo.onfit.video.repository;

import kspo.onfit.video.domain.FitnessVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FitnessVideoRepository extends JpaRepository<FitnessVideo, Long> {
    // Like 쿼리 (느림) - 필요시 사용
    Optional<FitnessVideo> findFirstByTitleContaining(String keyword);
    
    // 완전 일치 쿼리 (빠름) - 기본 사용
    Optional<FitnessVideo> findFirstByTitle(String title);
}
