package kspo.onfit.like.voucherlike.service;

import kspo.onfit.like.voucherlike.domain.VoucherLike;
import kspo.onfit.member.domain.Member;
import kspo.onfit.member.service.MemberLowService;
import kspo.onfit.voucher.domain.Voucher;
import kspo.onfit.voucher.service.VoucherLowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoucherLikeService {

    private VoucherLikeLowService voucherLikeLowService;
    private MemberLowService memberLowService;
    private VoucherLowService voucherLowService;

    public Long createVoucherLike(Long voucherId, Long memberId){
        Voucher voucher = voucherLowService.getReferenceById(voucherId);
        Member member = memberLowService.getReferenceById(memberId);
        //TODO: 중복 확인
        VoucherLike voucherLike = new VoucherLike(voucher, member);
        VoucherLike savedVoucherLike = voucherLikeLowService.save(voucherLike);
        return savedVoucherLike.getId();
    }

    

}
