package kspo.onfit.like.voucherlike.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kspo.onfit.member.domain.Member;
import kspo.onfit.voucher.domain.Voucher;
import lombok.Getter;

@Getter
@Entity
public class VoucherLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public VoucherLike(Voucher voucher, Member member){
        this.voucher = voucher;
        this.member = member;
    }

    protected VoucherLike() {}

}
