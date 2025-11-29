package kspo.onfit.like.postlike.service;

import kspo.onfit.like.postlike.domain.PostLike;
import kspo.onfit.like.postlike.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeLowService {

    private final PostLikeRepository postLikeRepository;

    public PostLike save(PostLike postLike){
        return postLikeRepository.save(postLike);
    }

    public Boolean existsPostLikeByPostIdAndMemberId(Long postId, Long memberId){
        return postLikeRepository.existsPostLikeByPostIdAndMemberId(postId, memberId);
    }

    public Page<PostLike> findPostLikeByMemberId(Long memberId, Pageable pageable){
        return postLikeRepository.findPostLikeByMemberId(memberId, pageable);
    }

    public void removePostLikeByPostIdAndMemberId(Long postId, Long memberId){
        postLikeRepository.removeByPostIdAndMemberId(postId, memberId);
    }

}