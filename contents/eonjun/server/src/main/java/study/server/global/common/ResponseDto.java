package study.server.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
// builder
public class ResponseDto<T> {
  private String message;
  private T data;

  public static <T> ResponseDto<T> success(T data) {
    return new ResponseDto<>("success", data);
  }

  public static <T> ResponseDto<T> success(String message){
    return new ResponseDto<>(message, null); //- > null 수저엉 or true, false >>??
  }

  public static <T> ResponseDto<T> success(String message, T data) {
    return new ResponseDto<>(message, data);
  }
}
