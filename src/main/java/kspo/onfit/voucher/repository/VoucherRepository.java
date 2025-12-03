package kspo.onfit.voucher.repository;

import kspo.onfit.voucher.domain.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    @Override
    Voucher getReferenceById(Long id);

}
