package kspo.onfit.like.postlike.repository;

import java.util.List;
import kspo.onfit.like.postlike.domain.PostLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsPostLikeByPostIdAndMemberId(Long postId, Long memberId);

    @Query("select pl from PostLike pl join fetch pl.member join fetch pl.post where pl.member.id = :memberId")
    Page<PostLike> findPostLikeByMemberId(Long memberId, Pageable pageable);

    @Query("select pl from PostLike pl where pl.member.id = :memberId")
    List<PostLike> findPostLikeByMemberId(Long memberId);


    void removeByPostIdAndMemberId(Long postId, Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("delete from PostLike pl where pl.post.id = :postId")
    void removePostLikeByPostId(Long postId);

    @Query("select count(pl) from PostLike pl where pl.post.id = :postId")
    long countPostLike(Long postId);


}
