package kspo.onfit.like.voucherlike.repository;

import java.util.List;
import kspo.onfit.like.voucherlike.domain.VoucherLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VoucherLikeRepository extends JpaRepository<VoucherLike, Long> {
    
    boolean existsVoucherLikeByVoucherIdAndMemberId(Long voucherId, Long memberId);
    
    void removeByVoucherIdAndMemberId(Long voucherId, Long memberId);

    @Query(
            value = "select vl from VoucherLike vl join fetch vl.member join fetch vl.voucher where vl.member.id = :memberId",
            countQuery = "select count(vl) from VoucherLike vl where vl.member.id = :memberId"
    )
    Page<VoucherLike> findVoucherLikeByMemberIdWithMemberAndVoucher(Long memberId, Pageable pageable);

    @Query("select vl from VoucherLike vl where vl.member.id = :memberId")
    List<VoucherLike> findVoucherLikeByMemberId(Long memberId);
    
}
