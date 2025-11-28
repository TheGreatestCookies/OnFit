package kspo.onfit.global.Exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public BadRequestException(ExceptionCode exceptionCode) {
        super(exceptionCode.getErrorMessage());
        this.exceptionCode = exceptionCode;
    }
}
