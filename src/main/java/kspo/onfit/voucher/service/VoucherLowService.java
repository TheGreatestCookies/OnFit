package kspo.onfit.voucher.service;

import kspo.onfit.voucher.domain.Voucher;
import kspo.onfit.voucher.repository.QVoucherRepository;
import kspo.onfit.voucher.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoucherLowService {

    private final QVoucherRepository qVoucherRepository;

    private final VoucherRepository voucherRepository;

    public Page<Voucher> findVoucherByAreaAndSports(String area, String sports, Pageable pageable){
        return qVoucherRepository.findVoucherByAreaAndSports(area, sports, pageable);
    }

    public Voucher getReferenceById(Long id){
        return voucherRepository.getReferenceById(id);
    }


}
