package com.example.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Builder
@Getter
public class ErrorResponse {

    private Integer code;
    private String name;
    private String message;

    // 필드-오류메시지 저장
    private Map<String, String> errors;

    // 비즈니스 로직 예외처리
    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .code(errorCode.getCode())
                        .name(errorCode.name())
                        .message(errorCode.getMessage())
                        .build()
                );
    }

    // 컨트롤러단 유효성 검사 예외처리
    public static ResponseEntity<ErrorResponse> validationFailed(Map<String, String> errors) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .code(errorCode.getCode())
                        .name(errorCode.name())
                        .message(errorCode.getMessage())
                        .errors(errors)
                        .build());
    }
}
