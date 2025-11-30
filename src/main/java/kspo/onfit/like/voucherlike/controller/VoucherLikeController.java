package kspo.onfit.like.voucherlike.controller;

import java.net.URI;
import kspo.onfit.like.voucherlike.service.VoucherLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/voucher")
public class VoucherLikeController {

    private VoucherLikeService voucherLikeService;

    private final Long memberId = 2L;

    @PostMapping("/{voucherId}/like")
    public ResponseEntity<Void> createPostLike(
            @PathVariable Long voucherId
    ) {
        Long savedId = voucherLikeService.createVoucherLike(voucherId, memberId);
        return ResponseEntity.created(URI.create(String.format("/api/post/%d/like/%d", voucherId, savedId))).build();
    }


}
