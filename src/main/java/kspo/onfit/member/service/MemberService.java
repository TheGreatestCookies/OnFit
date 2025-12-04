package kspo.onfit.member.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kspo.onfit.global.Exception.BadRequestException;
import kspo.onfit.global.Exception.EntityNotFoundException;
import kspo.onfit.global.Exception.ExceptionCode;
import kspo.onfit.member.domain.Member;
import kspo.onfit.member.dto.LoginRequest;
import kspo.onfit.member.dto.MemberResponse;
import kspo.onfit.member.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberLowService memberLowService;
    private final PasswordEncoder passwordEncoder;

    public MemberResponse signUp(SignUpRequest request) {
        if (memberLowService.existsByEmail(request.email())) {
            throw new BadRequestException(ExceptionCode.MEMBER_EMAIL_DUPLICATE);
        }

        Integer profileImageNumber = request.profileImageNumber();
        if (profileImageNumber < 1 || profileImageNumber > 5) {
            throw new BadRequestException(ExceptionCode.MEMBER_PROFILE_IMAGE_INVALID);
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        Member member = Member.builder()
                .email(request.email())
                .name(request.name())
                .pw(encodedPassword)
                .profileImageNumber(profileImageNumber)
                .build();

        Member savedMember = memberLowService.save(member);
        return MemberResponse.from(savedMember, savedMember.getProfileImageNumber());
    }

    public MemberResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        Member member = memberLowService.findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException(ExceptionCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), member.getPw())) {
            throw new BadRequestException(ExceptionCode.MEMBER_PASSWORD_INVALID);
        }

        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("loginMember", member);

        return MemberResponse.from(member, member.getProfileImageNumber());
    }

    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}

