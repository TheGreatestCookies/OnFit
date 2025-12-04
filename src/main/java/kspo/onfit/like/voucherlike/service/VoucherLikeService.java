package kspo.onfit.like.voucherlike.service;

import kspo.onfit.global.Exception.EntityDuplicateException;
import kspo.onfit.global.Exception.ExceptionCode;
import kspo.onfit.like.voucherlike.domain.VoucherLike;
import kspo.onfit.member.domain.Member;
import kspo.onfit.member.service.MemberLowService;
import kspo.onfit.redis.RedisUtil;
import kspo.onfit.voucher.domain.Voucher;
import kspo.onfit.voucher.dto.VoucherResponseDto;
import kspo.onfit.voucher.service.VoucherLowService;
import kspo.onfit.voucher.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class VoucherLikeService {

    private final VoucherLikeLowService voucherLikeLowService;
    private final MemberLowService memberLowService;
    private final VoucherLowService voucherLowService;
    private final VoucherService voucherService;
    private final RedisUtil redisUtil;

    public Long createVoucherLike(Long voucherId, Long memberId){
        if(voucherLikeLowService.existsVoucherLikeByVoucherIdAndMemberId(voucherId, memberId)){
            throw new EntityDuplicateException(ExceptionCode.VOUCHER_LIKE_DUPLICATE);
        }
        Voucher voucher = voucherLowService.getReferenceById(voucherId);
        Member member = memberLowService.getReferenceById(memberId);
        VoucherLike voucherLike = new VoucherLike(voucher, member);
        VoucherLike savedVoucherLike = voucherLikeLowService.save(voucherLike);

        //캐시 무효화
        String key = String.format("voucher:%d:like_count", voucherId);
        redisUtil.delete(key);

        return savedVoucherLike.getId();
    }

    public void removeVoucherLike(Long voucherId, Long memberId){
        voucherLikeLowService.removeByVoucherIdAndMemberId(voucherId, memberId);

        //캐시 무효화
        String key = String.format("voucher:%d:like_count", voucherId);
        redisUtil.delete(key);
    }

    public Page<VoucherResponseDto> getMyVoucherLikeList(Long memberId, Pageable pageable){
        Page<VoucherLike> voucherLikes = voucherLikeLowService.findVoucherLikeByMemberIdWithMemberAndVoucher(memberId, pageable);
        return voucherLikes.map(
                voucherLike ->
                        new VoucherResponseDto(
                                voucherLike.getVoucher(),
                                voucherService.countVoucherLikes(voucherLike.getVoucher().getId()),
                                true
                        )
        );
    }

}
