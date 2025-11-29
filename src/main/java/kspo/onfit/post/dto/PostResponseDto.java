package kspo.onfit.post.dto;

import java.time.LocalDateTime;
import java.util.List;
import kspo.onfit.like.postlike.domain.PostLike;
import kspo.onfit.post.domain.Post;

public record PostResponseDto(

        //postInfo
        Long id,
        String tile,
        String content,
        LocalDateTime createdTime,
        List<String> imageUrls,

        //author Info
        Long userId,
        String userName,
        String profileImage
)
{
    public PostResponseDto(Post post, List<String> imageUrls){
        this(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                imageUrls,
                post.getMember().getId(),
                post.getMember().getName(),
                post.getMember().getProfileImage()
        );
    }

    public PostResponseDto(PostLike postLike, List<String> imageUrls){
        this(
                postLike.getPost().getId(),
                postLike.getPost().getTitle(),
                postLike.getPost().getContent(),
                postLike.getPost().getCreatedAt(),
                imageUrls,

                postLike.getMember().getId(),
                postLike.getMember().getName(),
                postLike.getMember().getProfileImage()
        );
    }
}
