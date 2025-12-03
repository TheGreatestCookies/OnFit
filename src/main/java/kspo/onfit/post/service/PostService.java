package kspo.onfit.post.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kspo.onfit.global.Exception.BadRequestException;
import kspo.onfit.global.Exception.ExceptionCode;
import kspo.onfit.imageFile.domain.ImageFile;
import kspo.onfit.imageFile.service.ImageFileService;
import kspo.onfit.imageFile.service.S3Service;
import kspo.onfit.like.postlike.service.PostLikeLowService;
import kspo.onfit.member.domain.Member;
import kspo.onfit.member.service.MemberLowService;
import kspo.onfit.post.domain.Post;
import kspo.onfit.post.dto.PostRequestDto;
import kspo.onfit.post.dto.PostResponseDto;
import kspo.onfit.post.dto.PostUpdateDto;
import kspo.onfit.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final RedisUtil redisUtil;
    private final PostLowService postLowService;
    private final PostLikeLowService postLikeLowService;
    private final MemberLowService memberLowService;
    private final ImageFileService imageFileService;
    private final S3Service s3Service;

    public Long writePost(PostRequestDto postRequestDto, Long userId){
        if(!checkPosts(userId)){
            throw new BadRequestException(ExceptionCode.POST_LIMIT);
        }
        Member member = memberLowService.getReferenceById(userId);
        Post post = new Post(postRequestDto, member);
        Post savedPost = postLowService.savePost(post);
        imageFileService.saveImages(post.getId(), postRequestDto.imagesUrls()); // 데이터베이스에 이미지 url 저장
        return savedPost.getId();
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getAllPost(Long memberId, Pageable pageable){

        Page<Post> posts =  postLowService.findAllByOrderByCreatedAtDesc(pageable);

        List<Long> postIds = posts.getContent().stream()
                .map(post -> post.getId())
                .toList();

        Map<Long, List<String>> postImages = getPostsImages(postIds);

        List<Long> likedPost;
        if(memberId == null){
            likedPost = List.of(); // 로그인을 수행하지 않은 경우
        }
        else{
            likedPost = postLikeLowService.findPostLikeByMemberId(memberId)
                    .stream().map(postLike -> postLike.getPost().getId())
                    .toList();
        }

        return posts.map(
                post ->
                        new PostResponseDto(
                                post,
                                postImages.getOrDefault(post.getId(), List.of()),
                                countPostLikes(post.getId()),
                                likedPost.contains(post.getId())
                        )
        );
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getMyPosts(Long memberId, Pageable pageable){
        Page<Post> posts =  postLowService.findMyPostsByMemberId(memberId, pageable);

        List<Long> postIds = posts.getContent().stream()
                .map(post -> post.getId())
                .toList();

        List<Long> likedPost = postLikeLowService.findPostLikeByMemberId(memberId)
                .stream()
                .map(postLike -> postLike.getPost().getId())
                .toList();

        Map<Long, List<String>> postImages = getPostsImages(postIds);
        return posts.map(post -> new PostResponseDto(
                post,
                postImages.getOrDefault(post.getId(), List.of()),
                countPostLikes(post.getId()),
                likedPost.contains(post.getId())
        ));
    }

    @Transactional(readOnly = true)
    public Boolean checkPosts(Long memberId){
        LocalDate today = LocalDate.now();
        int count = postLowService.findMyPostsByMemberIdAndDate(memberId, today.atStartOfDay(), today.plusDays(1).atStartOfDay());
        return count <= 0;
    }

    public Long updatePost(Long id, Long memberId, PostUpdateDto postUpdateDto) {
        Post post = postLowService.findPostByIdAndMemberId(id, memberId);

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

    public void removePost(Long id, Long memberId){
        Post post = postLowService.findPostByIdAndMemberId(id, memberId);

        postLikeLowService.removePostLikeByPostId(id);

        List<String> imageUrls = imageFileService.getImagesByPostId(id);
        imageFileService.deleteImageFiles(id);
        postLowService.removePostById(id);
        s3Service.deleteImageFiles(imageUrls);
    }

    public Map<Long, List<String>> getPostsImages(List<Long> postIds){
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

    public Long countPostLikes(Long postId) {
        String key = String.format("post:%d:like_count", postId);
        Optional<String> cachedResult = redisUtil.select(key);
        if (cachedResult.isEmpty()) {
            //db에 직접 조회하고
            Long value = postLikeLowService.countPostLike(postId);
            redisUtil.insert(key, value.toString());
            return value;
        }
        return Long.parseLong(cachedResult.get());
    }


}
