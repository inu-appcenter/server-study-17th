package com.blog.appcenter_server_week2.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponseDto {

    private final String code;
    private final String message;

    public static ResponseEntity<ErrorResponseDto> from(final ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponseDto.builder()
                        .code(errorCode.name())
                        .message(errorCode.getMessage())
                        .build()
                );
    }
}
