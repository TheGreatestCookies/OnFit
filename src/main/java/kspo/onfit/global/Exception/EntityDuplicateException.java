package kspo.onfit.global.Exception;

import lombok.Getter;

@Getter
public class EntityDuplicateException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public EntityDuplicateException(ExceptionCode exceptionCode) {
        super(exceptionCode.getErrorMessage());
        this.exceptionCode = exceptionCode;
    }
}
