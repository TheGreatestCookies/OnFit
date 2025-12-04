package kspo.onfit.chatbot.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kspo.onfit.chatbot.domain.HomeWorkoutRecommendationLog;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class HomeWorkoutRecommendationResponseDto {
    private Long id;
    private List<String> warmupExercises;
    private List<String> mainExercises;
    private List<String> coolDownExercises;
    private List<VideoInfo> warmupVideos;
    private List<VideoInfo> mainVideos;
    private List<VideoInfo> coolDownVideos;
    private String moodTags;
    private LocalDateTime createdAt;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Getter
    @Builder
    public static class VideoInfo {
        private String title;
        private String youtubeCode;
    }

    public static HomeWorkoutRecommendationResponseDto from(HomeWorkoutRecommendationLog log) {
        return HomeWorkoutRecommendationResponseDto.builder()
                .id(log.getId())
                .warmupExercises(parseExercises(log.getWarmupExercises()))
                .mainExercises(parseExercises(log.getMainExercises()))
                .coolDownExercises(parseExercises(log.getCoolDownExercises()))
                .warmupVideos(parseVideos(log.getWarmupVideosJson()))
                .mainVideos(parseVideos(log.getMainVideosJson()))
                .coolDownVideos(parseVideos(log.getCoolDownVideosJson()))
                .moodTags(log.getMoodTags())
                .createdAt(log.getCreatedAt())
                .build();
    }

    private static List<String> parseExercises(String exercises) {
        if (exercises == null || exercises.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(exercises.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private static List<VideoInfo> parseVideos(String videosJson) {
        if (videosJson == null || videosJson.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(videosJson, new TypeReference<List<VideoInfo>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}

