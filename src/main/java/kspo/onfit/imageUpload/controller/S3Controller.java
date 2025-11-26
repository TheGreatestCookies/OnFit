package kspo.onfit.imageUpload.controller;

import kspo.onfit.imageUpload.dto.PreSignedResponseDto;
import kspo.onfit.imageUpload.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/s3")
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping
    public ResponseEntity<PreSignedResponseDto> getPreSignedUrl(){
        return ResponseEntity.ok(s3Service.getPreSignedUrl());
    }

}
