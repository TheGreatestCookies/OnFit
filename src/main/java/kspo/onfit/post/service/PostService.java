package kspo.onfit.post.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kspo.onfit.imageFile.domain.ImageFile;
import kspo.onfit.imageFile.service.ImageFileService;
import kspo.onfit.imageFile.service.S3Service;
import kspo.onfit.post.domain.Post;
import kspo.onfit.post.dto.PostRequestDto;
import kspo.onfit.post.dto.PostResponseDto;
import kspo.onfit.post.dto.PostUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostLowService postLowService;
    private final ImageFileService imageFileService;
    private final S3Service s3Service;

    public Long writePost(PostRequestDto postRequestDto){
        Post post = new Post(postRequestDto);
        Post savedPost = postLowService.savePost(post);
        imageFileService.saveImages(post.getId(), postRequestDto.imagesUrls()); // 데이터베이스에 이미지 url 저장
        return savedPost.getId();
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getAllPost(Pageable pageable){
        Page<Post> posts =  postLowService.findAllByOrderByCreatedAtDesc(pageable);
        List<Long> postIds = posts.map(post -> post.getId()).toList();
        Map<Long, List<String>> postImages = getPostsImages(postIds);
        return posts.map(post -> new PostResponseDto(post, postImages.getOrDefault(post.getId(), List.of())));
    }

    public Long updatePost(Long id, PostUpdateDto postUpdateDto) {
        //TODO: id로 본인 확인 하기
        Post post = postLowService.findPostById(id);

        //이미지 수정
        List<String> previousImages = imageFileService.getImagesByPostId(post.getId());
        List<String> updateDtoImages = postUpdateDto.imagesUrls();

        List<String> deleteImages = new ArrayList<>(previousImages);
        deleteImages.removeAll(updateDtoImages);

        List<String> newImages = new ArrayList<>(updateDtoImages);
        newImages.removeAll(previousImages);

        //글 수정
        post.updateContent(postUpdateDto);

        //1. 삭제
        imageFileService.deleteImageFilesByUrls(deleteImages);

        //2. 저장
        imageFileService.saveImages(post.getId(), newImages);

        //삭제된 이미지 s3에서 삭제하기
        s3Service.deleteImageFiles(deleteImages);
        return post.getId();
    }

    public void removePost(Long id){
        //TODO: id로 본인 확인 하기
        List<String> imageUrls = imageFileService.getImagesByPostId(id);
        imageFileService.deleteImageFiles(id);
        postLowService.removePostById(id);
        s3Service.deleteImageFiles(imageUrls);
    }

    private Map<Long, List<String>> getPostsImages(List<Long> postIds){
        Map<Long, List<String>> postImages = new HashMap<>();
        List<ImageFile> imageFileList = imageFileService.getImagesByPostIds(postIds);
        for(ImageFile imageFile : imageFileList){
            Long postId = imageFile.getPostId();
            String imageUrl = imageFile.getUrl();
            if(!postImages.containsKey(postId)){
                postImages.put(postId, new ArrayList<>());
            }
            postImages.get(postId).add(imageUrl);
        }
        return postImages;
    }


}
