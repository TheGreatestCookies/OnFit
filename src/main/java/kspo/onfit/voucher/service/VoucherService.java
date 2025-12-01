package kspo.onfit.voucher.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import kspo.onfit.like.voucherlike.domain.VoucherLike;
import kspo.onfit.like.voucherlike.service.VoucherLikeLowService;
import kspo.onfit.redis.RedisUtil;
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
    private final RedisUtil redisUtil;

    public Page<VoucherResponseDto> searchVoucherByAreaAndSports(String area, String sports, Long memberId, Pageable pageable) {

        List<Long> likedVoucherIds;
        if(memberId == null){
            likedVoucherIds = List.of(); // 로그인을 수행하지 않은 경우
        }
        else{
            likedVoucherIds = voucherLikeLowService.findVoucherLikeByMemberId(memberId)
                    .stream()
                    .map(voucherLike -> voucherLike.getVoucher().getId())
                    .toList();
        }

        return voucherLowService.findVoucherByAreaAndSports(area, sports, pageable).map(
                voucher ->
                        new VoucherResponseDto(
                                voucher,
                                countVoucherLikes(voucher.getId()),
                                likedVoucherIds.contains(voucher.getId())
                        )
        );
    }

    public Long countVoucherLikes(Long voucherId) {
        String key = String.format("voucher:%d:like_count", voucherId);
        Optional<String> cachedResult = redisUtil.select(key);
        if (cachedResult.isEmpty()) {
            //db에 직접 조회하고
            Long value = voucherLikeLowService.countVoucherLikeByVoucherId(voucherId);
            redisUtil.insert(key, value.toString());
            return value;
        }
        return Long.parseLong(cachedResult.get());
    }


}