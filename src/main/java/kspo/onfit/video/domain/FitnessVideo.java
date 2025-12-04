package kspo.onfit.video.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "fitness_video")
public class FitnessVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "youtube_code", nullable = false, length = 32)
    private String youtubeCode;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(nullable = false, length = 500)
    private String thumbnail;

}
