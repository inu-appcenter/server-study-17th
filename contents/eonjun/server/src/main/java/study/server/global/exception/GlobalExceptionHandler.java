package study.server.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import study.server.global.common.ResponseDto;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(annotations = RestController.class, basePackages = {"study.server.domain.user"})
public class GlobalExceptionHandler {

  // 유효성 검사 실패 처리
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseDto<?>> handleValidationException(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors()
      .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    return ResponseEntity
      .status(400)  // -> 커스텀 에러코드로 수정
      .body(ResponseDto.error(400, "입력값 오류", errors));
  }

  // 공통 CustomException 처리
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ResponseDto<?>> handleCustomException(CustomException ex) {
    ErrorCode code = ex.getErrorCode();
    return ResponseEntity
      .status(code.getStatus())
      .body(ResponseDto.error(code.getStatus().value(), code.getMessage()));
  }

  // 예상 못한 모든 예외 처리
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseDto<?>> handleAll(Exception ex) {
    return ResponseEntity
      .status(500)
      .body(ResponseDto.error(500, "서버 내부 오류: " + ex.getMessage()));
  }
}
