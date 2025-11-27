package kspo.onfit.voucher.controller;

import kspo.onfit.voucher.dto.VoucherResponseDto;
import kspo.onfit.voucher.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/voucher")
public class VoucherController {

    public final VoucherService voucherService;

    @GetMapping
    public ResponseEntity<Page<VoucherResponseDto>> searchVoucher(
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String sports,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Page<VoucherResponseDto> voucherResponseDtos =
                voucherService.searchVoucherByAreaAndSports(area, sports, PageRequest.of(page, size));
        return ResponseEntity.ok(voucherResponseDtos);
    }

}
