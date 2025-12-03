package kspo.onfit.like.voucherlike.service;

import java.util.List;
import kspo.onfit.like.voucherlike.domain.VoucherLike;
import kspo.onfit.like.voucherlike.repository.VoucherLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoucherLikeLowService {

    private final VoucherLikeRepository voucherLikeRepository;

    public VoucherLike save(VoucherLike voucherLike){
        return voucherLikeRepository.save(voucherLike);
    }

    public boolean existsVoucherLikeByVoucherIdAndMemberId(Long voucherId, Long memberId){
        return voucherLikeRepository.existsVoucherLikeByVoucherIdAndMemberId(voucherId, memberId);
    }

    public void removeByVoucherIdAndMemberId(Long voucherId, Long memberId){
        voucherLikeRepository.removeByVoucherIdAndMemberId(voucherId, memberId);
    }

    public Page<VoucherLike> findVoucherLikeByMemberIdWithMemberAndVoucher(Long memberId, Pageable pageable){
        return voucherLikeRepository.findVoucherLikeByMemberIdWithMemberAndVoucher(memberId, pageable);
    }

    public List<VoucherLike> findVoucherLikeByMemberId(Long memberId){
        return voucherLikeRepository.findVoucherLikeByMemberId(memberId);
    }

    public Long countVoucherLikeByVoucherId(Long voucherId){
        return voucherLikeRepository.countVoucherLikeByVoucherId(voucherId);
    }


}
