package study.server.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
// builder
public class ErrorResponseDto<T> {
  private T status;
  private String message;

  public static <T> ErrorResponseDto<T> error(T status, String message) {
    return new ErrorResponseDto<>(status, message);
  }
}
