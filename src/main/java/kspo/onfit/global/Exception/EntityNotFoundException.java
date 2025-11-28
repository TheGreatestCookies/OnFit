package kspo.onfit.global.Exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public EntityNotFoundException(ExceptionCode exceptionCode) {
        super(exceptionCode.getErrorMessage());
        this.exceptionCode = exceptionCode;
    }
}
