package kspo.onfit.global.Exception;

import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public ForbiddenException(ExceptionCode exceptionCode) {
        super(exceptionCode.getErrorMessage());
        this.exceptionCode = exceptionCode;
    }
}
