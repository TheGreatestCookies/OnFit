package kspo.onfit.voucher.service;

import java.util.List;
import kspo.onfit.like.voucherlike.domain.VoucherLike;
import kspo.onfit.like.voucherlike.service.VoucherLikeLowService;
import kspo.onfit.voucher.dto.VoucherResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherLowService voucherLowService;
    private final VoucherLikeLowService voucherLikeLowService;

    public Page<VoucherResponseDto> searchVoucherByAreaAndSports(String area, String sports, Long memberId, Pageable pageable) {

        List<Long> likedVoucherIds = voucherLikeLowService.findVoucherLikeByMemberId(memberId)
                .stream()
                .map(voucherLike -> voucherLike.getVoucher().getId())
                .toList();

        return voucherLowService.findVoucherByAreaAndSports(area, sports, pageable)
                .map(voucher -> new VoucherResponseDto
                        (
                                voucher,
                                likedVoucherIds.contains(voucher.getId())
                        )
                );
    }

}