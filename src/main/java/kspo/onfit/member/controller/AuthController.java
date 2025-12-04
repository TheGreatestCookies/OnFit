package kspo.onfit.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kspo.onfit.member.dto.LoginRequest;
import kspo.onfit.member.dto.MemberResponse;
import kspo.onfit.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관리 API")
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하여 세션을 생성합니다.")
    public ResponseEntity<MemberResponse> login(@RequestBody @Valid LoginRequest request,
            HttpServletRequest httpRequest) {
        return ResponseEntity.ok(memberService.login(request, httpRequest));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "현재 세션을 만료시킵니다.")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        memberService.logout(request);
        return ResponseEntity.ok().build();
    }
}
