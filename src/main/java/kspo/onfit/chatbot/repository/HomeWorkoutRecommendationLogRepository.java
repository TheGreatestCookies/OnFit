package kspo.onfit.chatbot.repository;

import kspo.onfit.chatbot.domain.HomeWorkoutRecommendationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeWorkoutRecommendationLogRepository extends JpaRepository<HomeWorkoutRecommendationLog, Long> {
    List<HomeWorkoutRecommendationLog> findByMemberIdOrderByCreatedAtDesc(Long memberId);
}

