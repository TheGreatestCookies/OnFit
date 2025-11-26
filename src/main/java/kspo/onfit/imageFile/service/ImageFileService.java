package kspo.onfit.imageFile.service;

import java.util.List;
import kspo.onfit.imageFile.domain.ImageFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageFileService {

    private final ImageFileLowService imageFileLowService;

    public void saveImages(Long postId, List<String> imageUrls){
        List<ImageFile> imageFiles = imageUrls.stream()
                .map(imageUrl -> new ImageFile(imageUrl, postId))
                .toList();
        imageFileLowService.save(imageFiles);
    }

    public List<String> getImagesByPostId(Long postId){
        return imageFileLowService.findImageFileByPostId(postId)
                .stream()
                .map(imageFile -> imageFile.getUrl())
                .toList();
    }

    public List<ImageFile> getImagesByPostIds(List<Long> postIds){
        return imageFileLowService.findImageFileByPostIds(postIds);
    }

    public void deleteImageFiles(Long postId){
        imageFileLowService.deleteImagesByPostId(postId);
    }

    public void deleteImageFilesByUrls(List<String> urls){
        imageFileLowService.deleteImageFilesByUrls(urls);
    }
    
}
