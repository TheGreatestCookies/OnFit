package kspo.onfit.post.dto;

import java.time.LocalDateTime;
import java.util.List;
import kspo.onfit.post.domain.Post;

public record PostResponseDto(
        Long id,
        String content,
        LocalDateTime createdTime,
        List<String> imageUrls,

        Long userId,
        String userName,
        String profileImage
)
{
    public PostResponseDto(Post post, List<String> imageUrls){
        this(
                post.getId(),
                post.getContent(),
                post.getCreatedAt(),
                imageUrls,
                post.getMember().getId(),
                post.getMember().getName(),
                post.getMember().getProfileImage()
        );
    }
}
