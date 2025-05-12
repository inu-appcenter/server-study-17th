package Appcenter.study.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseEntity> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return ErrorResponseEntity.toResponseEntity(errorCode,"요청한 값이 올바르지 않습니다.", errors);
    }
}
