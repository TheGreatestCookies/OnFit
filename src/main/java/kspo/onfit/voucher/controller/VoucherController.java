package kspo.onfit.voucher.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Voucher API", description = "이용권 조회를 위한 API")
public class VoucherController {

    public final VoucherService voucherService;

    @GetMapping
    @Operation(summary = "지역과 종목을 기준으로 이용권 조회")
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
