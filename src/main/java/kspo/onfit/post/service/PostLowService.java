package kspo.onfit.post.service;

import java.time.LocalDateTime;
import kspo.onfit.global.Exception.BadRequestException;
import kspo.onfit.global.Exception.ExceptionCode;
import kspo.onfit.global.Exception.ForbiddenException;
import kspo.onfit.post.domain.Post;
import kspo.onfit.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLowService {

    private final PostRepository postRepository;

    public Post savePost(Post post){
        return postRepository.save(post);
    }

    public Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable){
        return postRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Page<Post> findMyPostsByMemberId(Long memberId, Pageable pageable){
        return postRepository.findPostsByMemberId(memberId, pageable);
    }

    public int findMyPostsByMemberIdAndDate(Long memberId, LocalDateTime today, LocalDateTime tomorrow){
        return postRepository.countTodayPostByMemberId(memberId, today, tomorrow);
    }

    public Post findPostByIdAndMemberId(Long id, Long memberId){
        return postRepository.findPostByIdAndMemberId(id, memberId)
                .orElseThrow(() -> new ForbiddenException(ExceptionCode.POST_FORBIDDEN));
    }

    public void removePostById(Long id){
        postRepository.removePostById(id);
    }

}
