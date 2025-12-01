package kspo.onfit.voucher.dto;

import kspo.onfit.voucher.domain.Voucher;

public record VoucherResponseDto(

        Long id,
        String name,
        String area,
        Integer areaCode,
        String facilityName,
        String sports,
        Integer sportsCode,
        Integer sigunguCode,
        String sigunguName,
        String addr1,
        String addr2,
        String zipCode,
        String telephone,
        Integer memberCount,
        Integer price,

        Boolean myLike

){
    public VoucherResponseDto(Voucher voucher, Boolean myLike){
        this(
                voucher.getId(),
                voucher.getName(),
                voucher.getArea(),
                voucher.getAreaCode(),
                voucher.getFacilityName(),
                voucher.getSports(),
                voucher.getSportsCode(),
                voucher.getSigunguCode(),
                voucher.getSigunguName(),
                voucher.getAddr1(),
                voucher.getAddr2(),
                voucher.getZipCode(),
                voucher.getTelephone(),
                voucher.getMemberCount(),
                voucher.getPrice(),

                myLike
        );
    }

}