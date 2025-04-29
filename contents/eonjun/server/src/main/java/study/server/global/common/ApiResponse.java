package study.server.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
// builder
public class ApiResponse<T> {
  private int status;
  private String message;
  private T data;

  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(HttpStatus.OK.value(), "success", data);
  }

  public static <T> ApiResponse<T> success(String message){
    return new ApiResponse<>(HttpStatus.OK.value(), message, null); //- > null 수저엉 or true, false >>??
  }

  public static <T> ApiResponse<T> success(String message, T data) {
    return new ApiResponse<>(HttpStatus.OK.value(), message, data);
  }

  public static <T> ApiResponse<T> error(int status, String message) {
    return new ApiResponse<>(status, message, null);
  }
}
