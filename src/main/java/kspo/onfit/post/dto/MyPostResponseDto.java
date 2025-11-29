package kspo.onfit.post.dto;

import java.time.LocalDateTime;
import java.util.List;
import kspo.onfit.post.domain.Post;

public record MyPostResponseDto(
        Long id,
        String title,
        String content,
        LocalDateTime createdTime,
        List<String> imageUrls
)
{
    public MyPostResponseDto(Post post, List<String> imageUrls){
        this(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                imageUrls
        );
    }
}
