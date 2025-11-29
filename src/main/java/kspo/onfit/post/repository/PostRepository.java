package kspo.onfit.post.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import kspo.onfit.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(
            value = "select p from Post p join fetch p.member order by p.createdAt desc",
            countQuery = "select count(p) from Post p"
    )
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Post> findPostsByMemberId(Long memberId, Pageable pageable);

    Optional<Post> findPostByIdAndMemberId(Long id, Long memberId);
    
    void removePostById(Long id);

    @Query("select count(p) from Post p where p.member.id =:id and p.createdAt >= :today and p.createdAt <:tomorrow")
    int countTodayPostByMemberId (Long id, LocalDateTime today, LocalDateTime tomorrow);

    @Override
    Post getReferenceById(Long id);
}
