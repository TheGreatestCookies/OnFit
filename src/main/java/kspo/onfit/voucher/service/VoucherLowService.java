package kspo.onfit.voucher.service;

import kspo.onfit.voucher.domain.Voucher;
import kspo.onfit.voucher.repository.QVoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoucherLowService {

    private final QVoucherRepository qVoucherRepository;

    public Page<Voucher> findVoucherByAreaAndSports(String area, String sports, Pageable pageable){
        return qVoucherRepository.findVoucherByAreaAndSports(area, sports, pageable);
    }
}
