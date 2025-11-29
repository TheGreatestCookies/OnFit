package kspo.onfit.like.postlike.service;

import java.util.List;
import java.util.Map;
import kspo.onfit.global.Exception.BadRequestException;
import kspo.onfit.global.Exception.EntityDuplicateException;
import kspo.onfit.global.Exception.ExceptionCode;
import kspo.onfit.like.postlike.domain.PostLike;
import kspo.onfit.member.domain.Member;
import kspo.onfit.member.service.MemberLowService;
import kspo.onfit.post.domain.Post;
import kspo.onfit.post.dto.PostResponseDto;
import kspo.onfit.post.service.PostLowService;
import kspo.onfit.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeLowService postLikeLowService;
    private final MemberLowService memberLowService;
    private final PostLowService postLowService;
    private final PostService postService;

    public Long createPostLike(Long postId, Long memberId){
        if(postLikeLowService.existsPostLikeByPostIdAndMemberId(postId, memberId)){
            throw new EntityDuplicateException(ExceptionCode.POST_LIKE_DUPLICATE); //중복 확인
        }
        Member member = memberLowService.getReferenceById(memberId);
        Post post = postLowService.getReferenceById(postId);
        PostLike postLike = new PostLike(post, member);
        PostLike saved = postLikeLowService.save(postLike);
        return saved.getId();
    }

    public void removePostLike(Long postId, Long memberId){
        postLikeLowService.removePostLikeByPostIdAndMemberId(postId, memberId);
    }

    public Page<PostResponseDto> getMyPostLikeList(Long memberId, Pageable pageable){
        Page<PostLike> postLikes = postLikeLowService.findPostLikeByMemberId(memberId, pageable);

        List<Long> postIds = postLikes.getContent()
                .stream()
                .map(postLike -> postLike.getPost().getId())
                .toList();

        Map<Long, List<String>> postImages = postService.getPostsImages(postIds);
        return postLikes.map(postLike -> new PostResponseDto(postLike, postImages.get(postLike.getPost().getId())));
    }
    
}