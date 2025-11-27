package kspo.onfit.imageFile.service;

import java.util.List;
import kspo.onfit.imageFile.domain.ImageFile;
import kspo.onfit.imageFile.repository.ImageFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageFileLowService {

    private final ImageFileRepository imageFileRepository;

    public void save(List<ImageFile> imageFiles){
        imageFileRepository.saveAll(imageFiles);
    }

    public List<ImageFile> findImageFileByPostId(Long postId){
        return imageFileRepository.findImageFileByPostId(postId);
    }

    public List<ImageFile> findImageFileByPostIds(List<Long> postIds){
        return imageFileRepository.findImageFileByPostIds(postIds);
    }

    public void deleteImagesByPostId(Long postId){
        imageFileRepository.removeImageFileByPostId(postId);
    }

    public void deleteImageFilesByUrls(List<String> urls){
        imageFileRepository.deleteImageFilesByUrls(urls);
    }

}