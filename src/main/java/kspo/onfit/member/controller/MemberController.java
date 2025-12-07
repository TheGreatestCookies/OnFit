package kspo.onfit.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kspo.onfit.member.domain.Member;
import kspo.onfit.member.dto.MemberResponse;
import kspo.onfit.member.dto.SignUpRequest;
import kspo.onfit.member.dto.UpdateProfileImageRequest;
import kspo.onfit.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 관리 API")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @Operation(summary = "회원가입", description = "새로운 회원을 등록합니다.")
    public ResponseEntity<MemberResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        MemberResponse response = memberService.signUp(request);
        return ResponseEntity.created(URI.create("/api/members/" + response.id())).body(response);
    }

    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "현재 로그인된 사용자의 정보를 조회합니다.")
    public ResponseEntity<MemberResponse> getMyInfo(
            @SessionAttribute(name = "loginMember", required = false) Member loginMember) {
        if (loginMember == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(MemberResponse.from(loginMember, loginMember.getProfileImageNumber()));
    }

    @PatchMapping("/me/profile-image")
    @Operation(summary = "프로필 이미지 번호 변경", description = "현재 로그인된 사용자의 프로필 이미지 번호를 변경합니다.")
    public ResponseEntity<MemberResponse> updateProfileImage(
            @RequestBody @Valid UpdateProfileImageRequest request,
            @SessionAttribute(name = "loginMember", required = false) Member loginMember,
            HttpServletRequest httpRequest) {
        if (loginMember == null) {
            return ResponseEntity.status(401).build();
        }
        
        MemberResponse response = memberService.updateProfileImage(loginMember, request.profileImageNumber());
        
        // 세션 업데이트: 변경된 Member 객체를 세션에 다시 저장
        Member updatedMember = memberService.findById(loginMember.getId());
        HttpSession session = httpRequest.getSession();
        session.setAttribute("loginMember", updatedMember);
        
        return ResponseEntity.ok(response);
    }
}
