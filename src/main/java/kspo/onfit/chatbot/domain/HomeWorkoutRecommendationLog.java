package kspo.onfit.chatbot.domain;

import jakarta.persistence.*;
import kspo.onfit.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "home_workout_recommendation_log")
public class HomeWorkoutRecommendationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String sessionId;

    @Column(columnDefinition = "TEXT")
    private String warmupExercises;

    @Column(columnDefinition = "TEXT")
    private String mainExercises;

    @Column(columnDefinition = "TEXT")
    private String coolDownExercises;

    @Column(columnDefinition = "TEXT")
    private String warmupVideosJson;

    @Column(columnDefinition = "TEXT")
    private String mainVideosJson;

    @Column(columnDefinition = "TEXT")
    private String coolDownVideosJson;

    @Column(length = 500)
    private String moodTags;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

