package Appcenter.study.global.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Data
@Builder
public class ErrorResponseEntity {

    private Integer code;
    private String name;
    private String message;
    private List<String> errors;

    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .code(errorCode.getCode())
                        .name(errorCode.name())
                        .message(errorCode.getMessage())
                        .build()
                );
    }

    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(ErrorCode errorCode, String message, List<String> errors) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .code(errorCode.getCode())
                        .name(errorCode.name())
                        .message(message)
                        .errors(errors)
                        .build()
                );
    }
}
