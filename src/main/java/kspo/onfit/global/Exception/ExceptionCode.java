package kspo.onfit.global.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    POST_LIMIT("하루에 하나의 포스트를 작성할 수 있습니다"),
    POST_FORBIDDEN("내가 작성한 포스트가 아닙니다");

    private final String errorMessage;

}
