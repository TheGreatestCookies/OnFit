package kspo.onfit.like.voucherlike.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import kspo.onfit.like.voucherlike.service.VoucherLikeService;
import kspo.onfit.voucher.dto.VoucherResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/vouchers")
@Tag(name = "VoucherLike API", description = "이용권 좋아요 기능을 위한 API")
public class VoucherLikeController {

    private final VoucherLikeService voucherLikeService;

    private final Long memberId = 2L;

    @Operation(summary = "이용권에 대한 좋아요 생성(누르기)")
    @PostMapping("/{voucherId}/like")
    public ResponseEntity<Void> createPostLike(
            @PathVariable Long voucherId
    ) {
        Long savedId = voucherLikeService.createVoucherLike(voucherId, memberId);
        return ResponseEntity.created(URI.create(String.format("/api/vouchers/%d/like/%d", voucherId, savedId))).build();
    }

    @Operation(summary = "이용권에 대한 좋아요 취소(삭제)")
    @DeleteMapping("/{voucherId}/like")
    public ResponseEntity<Void> removeVoucherLike(
            @PathVariable Long voucherId
    ){
        voucherLikeService.removeVoucherLike(voucherId, memberId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "내가 좋아요한 이용권 모두 조회하기")
    @GetMapping("/like/my")
    public ResponseEntity<Page<VoucherResponseDto>> getMyVoucherLikeList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Page<VoucherResponseDto> pagedList = voucherLikeService.getMyVoucherLikeList(memberId, PageRequest.of(page, size));
        return ResponseEntity.ok(pagedList);
    }


}
