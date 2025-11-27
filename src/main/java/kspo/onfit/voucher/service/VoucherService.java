package kspo.onfit.voucher.service;

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

    public Page<VoucherResponseDto> searchVoucherByAreaAndSports(String area, String sports, Pageable pageable){
        return voucherLowService.findVoucherByAreaAndSports(area, sports, pageable)
                .map(voucher -> new VoucherResponseDto(voucher));
    }

}