package kspo.onfit.imageFile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kspo.onfit.imageFile.dto.PreSignedResponseDto;
import kspo.onfit.imageFile.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/s3")
@Tag(name = "S3 API", description = "PreSignedUrl 발급을 위한 API")
public class S3Controller {

    private final S3Service s3Service;

    @Operation(summary = "이미지 업로드를 위한 PreSignedUrl 발급")
    @GetMapping
    public ResponseEntity<PreSignedResponseDto> getPreSignedUrl(){
        return ResponseEntity.ok(s3Service.getPreSignedUrl());
    }

}
