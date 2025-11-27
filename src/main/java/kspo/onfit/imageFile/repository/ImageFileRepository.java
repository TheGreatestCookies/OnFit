package kspo.onfit.imageFile.repository;

import java.util.List;
import kspo.onfit.imageFile.domain.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
    
    List<ImageFile> findImageFileByPostId(Long postId);

    @Query("select i from ImageFile i where i.postId in :postIds")
    List<ImageFile> findImageFileByPostIds(List<Long> postIds);

    @Modifying(clearAutomatically = true)
    @Query("delete from ImageFile i where i.postId =:postId")
    void removeImageFileByPostId(Long postId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from ImageFile i where i.url in :urls")
    void deleteImageFilesByUrls(List<String> urls);

}