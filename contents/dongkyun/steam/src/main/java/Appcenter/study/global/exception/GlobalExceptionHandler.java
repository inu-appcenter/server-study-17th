package Appcenter.study.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException ex) {
        return ErrorResponseEntity.toResponseEntity(ex.getErrorCode());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseEntity> handleAuthenticationException(AuthenticationException ex) {
        ErrorCode errorCode = ErrorCode.JWT_ENTRY_POINT;
        return ErrorResponseEntity.toResponseEntity(errorCode);
    }
}
