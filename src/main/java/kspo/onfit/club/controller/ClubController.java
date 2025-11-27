package kspo.onfit.club.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kspo.onfit.club.dto.ClubResponseDto;
import kspo.onfit.club.service.ClubService;
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
@RequestMapping("/api/club")
@Tag(name = "Club API", description = "동호회 조회를 위한 API")
public class ClubController {

    private final ClubService clubService;

    @GetMapping
    @Operation(summary = "지역과 종목을 기준으로 동호회 조회")
    public ResponseEntity<Page<ClubResponseDto>>searchClubArea(
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String sports,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size

    ) {
        Page<ClubResponseDto> clubResponseDtoPage = clubService.findClubByArea(area, sports, PageRequest.of(page, size));
        return ResponseEntity.ok(clubResponseDtoPage);
    }

}