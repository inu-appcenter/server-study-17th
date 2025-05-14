package study.server.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import study.server.global.common.ErrorResponseDto;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(annotations = RestController.class, basePackages = {"study.server.domain.user"})
public class GlobalExceptionHandler {

  // 유효성 검사 실패 처리
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDto<?>> handleValidationException(MethodArgumentNotValidException ex) {

    // todo : 이거 수정해야됨
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors()
      .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    ErrorCode errorCode = ErrorCode.INVALID_INPUT;
    return ResponseEntity
      .status(errorCode.getStatus())  // -> 커스텀 에러코드로 수정
      .body(ErrorResponseDto.error(errorCode.getCode(), errorCode.getMessage()));
  }

  // 공통 CustomException 처리
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponseDto<?>> handleCustomException(CustomException ex) {

    ErrorCode code = ex.getErrorCode();
    return ResponseEntity
      .status(code.getStatus())
      .body(ErrorResponseDto.error(code.getCode(), code.getMessage()));
  }

  // 예상 못한 모든 예외 처리
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto<?>> handleAll(Exception ex) {

    log.error(ex.getMessage(), ex);
    ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    return ResponseEntity
      .status(errorCode.getStatus())
      .body(ErrorResponseDto.error(errorCode.getCode(), errorCode.getMessage()));
  }
}
