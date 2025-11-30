package kspo.onfit.like.postlike.controller;

import java.net.URI;
import kspo.onfit.like.postlike.service.PostLikeService;
import kspo.onfit.post.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostLikeController {

    private final PostLikeService postLikeService;

    private final Long memberId = 2L;

    //좋아요 누르기
    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> createPostLike(
            @PathVariable Long postId
    ) {
        Long savedId = postLikeService.createPostLike(postId, memberId);
        return ResponseEntity.created(URI.create(String.format("/api/posts/%d/like/%d", postId, savedId))).build();
    }

    //좋아요 삭제하기
    @DeleteMapping("/{postId}/like")
    public ResponseEntity<Void> removePostLike(
            @PathVariable Long postId
    ) {
        postLikeService.removePostLike(postId, memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/like/my")
    public ResponseEntity<Page<PostResponseDto>> getMyPostLikeList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size

    ) {
        Page<PostResponseDto> pagedList = postLikeService.getMyPostLikeList(memberId, PageRequest.of(page, size));
        return ResponseEntity.ok(pagedList);
    }

}