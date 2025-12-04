package kspo.onfit.member.dto;

import kspo.onfit.member.domain.Member;

public record MemberResponse(
        Long id,
        String email,
        String name,
        Integer profileImageNumber
) {
    public static MemberResponse from(Member member, Integer profileImageNumber) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                profileImageNumber
        );
    }
}
