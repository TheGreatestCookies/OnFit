package kspo.onfit.like.voucherlike.service;

import kspo.onfit.like.voucherlike.domain.VoucherLike;
import kspo.onfit.like.voucherlike.repository.VoucherLikeRepository;

public class VoucherLikeLowService {

    private VoucherLikeRepository voucherLikeRepository;

    public VoucherLike save(VoucherLike voucherLike){
        return voucherLikeRepository.save(voucherLike);
    }

}
