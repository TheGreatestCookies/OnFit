package kspo.onfit.global;

import java.util.Map;
import kspo.onfit.global.Exception.BadRequestException;
import kspo.onfit.global.Exception.EntityDuplicateException;
import kspo.onfit.global.Exception.ForbiddenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String,String>> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getExceptionCode().getErrorMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String,String>> handleForbiddenException(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getExceptionCode().getErrorMessage()));
    }

    @ExceptionHandler(EntityDuplicateException.class)
    public ResponseEntity<Map<String,String>> handleEntityDuplicateException(EntityDuplicateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getExceptionCode().getErrorMessage()));
    }

}
