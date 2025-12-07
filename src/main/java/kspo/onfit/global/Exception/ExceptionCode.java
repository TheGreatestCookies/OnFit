package kspo.onfit.global.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    POST_LIMIT("하루에 하나의 포스트를 작성할 수 있습니다"),

    POST_LIKE_DUPLICATE("이미 좋아요를 누른 포스트입니다."),
    VOUCHER_LIKE_DUPLICATE("이미 좋아요를 누른 이용권입니다."),

    POST_FORBIDDEN("내가 작성한 포스트가 아닙니다"),

    MEMBER_EMAIL_DUPLICATE("이미 사용 중인 이메일입니다."),
    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다."),
    MEMBER_PASSWORD_INVALID("비밀번호가 일치하지 않습니다."),
    MEMBER_PROFILE_IMAGE_INVALID("프로필 이미지는 1번부터 6번까지 선택 가능합니다.");

    private final String errorMessage;

}
