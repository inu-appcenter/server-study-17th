package study.server.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 *
 * 에러 코드 관리
 */
@Getter
public enum ErrorCode {
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER404_1","사용자를 찾을 수 없습니다."),
  EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, "USER409_1","이미 등록된 이메일입니다."),
  USERNAME_DUPLICATED(HttpStatus.BAD_REQUEST, "USER409_2","이미 등록된 이름입니다."),
  INVALID_INPUT(HttpStatus.BAD_REQUEST, "USER400_3","입력값이 올바르지 않습니다."),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH401_1","인증이 필요합니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER500_1","서버 오류가 발생했습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;

  ErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
