package kspo.onfit.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import kspo.onfit.member.domain.Member;
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
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Tag(name = "Post API", description = "PreSignedUrl 발급을 위한 API")
public class PostController {

    private final PostService postService;

    @PostMapping
    @Operation(summary = "게시글 작성")
    public ResponseEntity<Void> createPost(
            @Valid @RequestBody PostRequestDto postRequestDto,
            @SessionAttribute(name = "loginMember", required = false) Member loginMember
    ){
        if (loginMember == null) {
            return ResponseEntity.status(401).build();
        }
        Long id = postService.writePost(postRequestDto, loginMember);
        return ResponseEntity.created(URI.create(String.format("/api/posts/%d", id))).build();
    }

    @GetMapping
    @Operation(summary = "모든 게시글 조회", description = "인증 없이도 조회 가능합니다. 로그인한 경우 좋아요 여부가 표시됩니다.")
    public ResponseEntity<Page<PostResponseDto>> getAllPost(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @SessionAttribute(name = "loginMember", required = false) Member loginMember
    ){
        Long memberId = loginMember != null ? loginMember.getId() : null;
        return ResponseEntity.ok(postService.getAllPost(memberId, PageRequest.of(page, size)));
    }

    @GetMapping("/my")
    public ResponseEntity<Page<PostResponseDto>> getMyPost(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @SessionAttribute(name = "loginMember", required = false) Member loginMember
    ){
        if (loginMember == null) {
            return ResponseEntity.status(401).build();
        }
        Page<PostResponseDto> postResponseDtos = postService.getMyPosts(loginMember.getId(), PageRequest.of(page, size));
        return ResponseEntity.ok(postResponseDtos);
    }

    @PatchMapping("/{postId}")
    @Operation(summary = "게시글 수정")
    public ResponseEntity<Long> updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateDto postUpdateDto,
            @SessionAttribute(name = "loginMember", required = false) Member loginMember
    ){
        if (loginMember == null) {
            return ResponseEntity.status(401).build();
        }
        Long updatedPostId = postService.updatePost(postId, loginMember.getId(), postUpdateDto);
        return ResponseEntity.ok(updatedPostId);
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "모든 게시글 삭제")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @SessionAttribute(name = "loginMember", required = false) Member loginMember
    ){
        if (loginMember == null) {
            return ResponseEntity.status(401).build();
        }
        postService.removePost(postId, loginMember.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    @Operation(summary = "오늘 작성한 글이 있는지 확인하는 메서드 - true : 글 작성 가능")
    public ResponseEntity<Boolean> checkTodayPost(
            @SessionAttribute(name = "loginMember", required = false) Member loginMember
    ){
        if (loginMember == null) {
            return ResponseEntity.status(401).build();
        }
        Boolean check = postService.checkPosts(loginMember.getId());
        return ResponseEntity.ok(check);
    }


}
