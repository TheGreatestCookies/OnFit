package kspo.onfit.post.service;

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

    public Post findPostById(Long id){
        return postRepository.findPostById(id);
    }

    public void removePostById(Long id){
        postRepository.removePostById(id);
    }

}
