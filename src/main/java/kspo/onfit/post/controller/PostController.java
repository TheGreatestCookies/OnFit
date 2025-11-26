package kspo.onfit.post.controller;

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
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(
            @Valid @RequestBody PostRequestDto postRequestDto
    ){
        Long id = postService.writePost(postRequestDto);
        return ResponseEntity.created(URI.create(String.format("/api/post/%d", id))).build();
    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getAllPost(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Page<PostResponseDto> postResponseDtos = postService.getAllPost(PageRequest.of(page, size));
        return ResponseEntity.ok(postResponseDtos);
    }

    @GetMapping("/my")
    public void getMyPost(){
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Long> updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateDto postUpdateDto
    ){
        Long updatedPostId = postService.updatePost(postId, postUpdateDto);
        return ResponseEntity.ok(updatedPostId);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId
    ){
        postService.removePost(postId);
        return ResponseEntity.noContent().build();
    }

}
