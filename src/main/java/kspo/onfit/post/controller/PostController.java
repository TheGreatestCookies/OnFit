package kspo.onfit.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import kspo.onfit.post.dto.PostRequestDto;
import kspo.onfit.post.dto.PostResponseDto;
import kspo.onfit.post.dto.PostUpdateDto;
import kspo.onfit.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Tag(name = "Post API", description = "PreSignedUrl 발급을 위한 API")
public class PostController {

    private final PostService postService;

    private final Long memberId = 2L; //temp

    @PostMapping
    @Operation(summary = "게시글 작성")
    public ResponseEntity<Void> createPost(
            @Valid @RequestBody PostRequestDto postRequestDto
    ){
        Long id = postService.writePost(postRequestDto, memberId);
        return ResponseEntity.created(URI.create(String.format("/api/post/%d", id))).build();
    }

    @GetMapping
    @Operation(summary = "모든 게시글 조회")
    public ResponseEntity<Page<PostResponseDto>> getAllPost(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Page<PostResponseDto> postResponseDtos = postService.getAllPost(memberId, PageRequest.of(page, size));
        return ResponseEntity.ok(postResponseDtos);
    }

    @GetMapping("/my")
    public ResponseEntity<Page<PostResponseDto>> getMyPost(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Page<PostResponseDto> postResponseDtos = postService.getMyPosts(memberId, PageRequest.of(page, size));
        return ResponseEntity.ok(postResponseDtos);
    }

    @PatchMapping("/{postId}")
    @Operation(summary = "게시글 수정")
    public ResponseEntity<Long> updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateDto postUpdateDto
    ){
        Long updatedPostId = postService.updatePost(postId, memberId, postUpdateDto);
        return ResponseEntity.ok(updatedPostId);
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "모든 게시글 삭제")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId
    ){
        postService.removePost(postId, memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    @Operation(summary = "오늘 작성한 글이 있는지 확인하는 메서드 - true : 글 작성 가능")
    public ResponseEntity<Boolean> checkTodayPost(){
        Boolean check = postService.checkPosts(memberId);
        return ResponseEntity.ok(check);
    }


}
