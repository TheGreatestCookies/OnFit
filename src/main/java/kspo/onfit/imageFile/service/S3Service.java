package kspo.onfit.imageFile.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import kspo.onfit.imageFile.dto.PreSignedResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class S3Service {

    @Value("${aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3Client;

    public PreSignedResponseDto getPreSignedUrl(){

        String fileName = UUID.randomUUID().toString();

        Date expiration = getPreSignedExpiration();

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);

        String preSignedUrl = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();

        return new PreSignedResponseDto(preSignedUrl);
    }

    //S3에서 이미지 직접 삭제
    public void deleteImageFile(String url){
        String key = extractKey(url);
        amazonS3Client.deleteObject(bucket, key);
    }

    //S3에서 이미지 직접 삭제 - 이미지 여러개
    public void deleteImageFiles(List<String> urls){
        urls.forEach(url -> deleteImageFile(url));
    }

    private Date getPreSignedExpiration(){
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += (1000 * 60) * 2; // 유효 시간 2분
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    private String extractKey(String url){
        String [] splitUrls = url.split("amazonaws.com/");
        return splitUrls[1];
    }

}
