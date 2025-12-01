package kspo.onfit.like.postlike.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "PostLike API", description = "게시글 좋아요 기능을 위한 API")
public class PostLikeController {

    private final PostLikeService postLikeService;

    private final Long memberId = 2L;

    @Operation(summary = "게시글에 대한 좋아요 생성(누르기)")
    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> createPostLike(
            @PathVariable Long postId
    ) {
        Long savedId = postLikeService.createPostLike(postId, memberId);
        return ResponseEntity.created(URI.create(String.format("/api/posts/%d/like/%d", postId, savedId))).build();
    }

    @Operation(summary = "게시글에 대한 좋아요 취소(삭제)")
    @DeleteMapping("/{postId}/like")
    public ResponseEntity<Void> removePostLike(
            @PathVariable Long postId
    ) {
        postLikeService.removePostLike(postId, memberId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "내가 좋아요한 게시글 모두 조회하기")
    @GetMapping("/like/my")
    public ResponseEntity<Page<PostResponseDto>> getMyPostLikeList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size

    ) {
        Page<PostResponseDto> pagedList = postLikeService.getMyPostLikeList(memberId, PageRequest.of(page, size));
        return ResponseEntity.ok(pagedList);
    }

}