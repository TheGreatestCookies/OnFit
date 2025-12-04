package kspo.onfit.chatbot.repository;

import kspo.onfit.chatbot.domain.VoucherRecommendationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoucherRecommendationLogRepository extends JpaRepository<VoucherRecommendationLog, Long> {
    List<VoucherRecommendationLog> findByMemberIdOrderByCreatedAtDesc(Long memberId);
}

