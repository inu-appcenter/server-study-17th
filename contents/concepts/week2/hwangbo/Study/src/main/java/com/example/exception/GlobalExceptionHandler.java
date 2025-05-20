package com.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 컨트롤러단 유효성 검사
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException e) {

        log.error("유효성 검사 실패");
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fe ->
                errors.put(fe.getField(), fe.getDefaultMessage())
        );

        return ErrorResponse.validationFailed(errors);
    }

    //서비스단 예외처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("에러 발생, 코드: {} 메시지: {}", e.getErrorCode(), e.getMessage());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    // 로그인 과정 비밀번호 불일치
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials() {
        ErrorCode code = ErrorCode.PASSWORD_NOT_CORRECT;
        log.error("에러 발생, 코드: {} 메시지: {}", code.getCode(), code.getMessage());
        return ErrorResponse.toResponseEntity(code);
    }

    // 핸들러에서 다루는 에러가 많아지면 아래처럼 로깅을 메소드로 만들어 사용하는 것도 좋을 듯
//    private ResponseEntity<ErrorResponse> handleErrorCode(ErrorCode code) {
//        log.error("에러 발생, 코드: {} 메시지: {}", code.getCode(), code.getMessage());
//        return ErrorResponse.toResponseEntity(code);
//    }
}
