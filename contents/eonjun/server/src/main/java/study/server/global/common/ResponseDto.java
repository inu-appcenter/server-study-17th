package study.server.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
// builder
public class ResponseDto<T> {
  private int status;
  private String message;
  private T data;

  public static <T> ResponseDto<T> success(T data) {
    return new ResponseDto<>(HttpStatus.OK.value(), "success", data);
  }

  public static <T> ResponseDto<T> success(String message){
    return new ResponseDto<>(HttpStatus.OK.value(), message, null); //- > null 수저엉 or true, false >>??
  }

  public static <T> ResponseDto<T> success(String message, T data) {
    return new ResponseDto<>(HttpStatus.OK.value(), message, data);
  }

  public static <T> ResponseDto<T> error(int status, String message) {
    return new ResponseDto<>(status, message, null);
  }

  public static <T> ResponseDto<T> error(int status, String message, T data) {
    return new ResponseDto<>(status, message, data);
  }
}
