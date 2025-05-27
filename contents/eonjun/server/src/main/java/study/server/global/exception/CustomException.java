package study.server.global.exception;

import lombok.Getter;

/**
 *
 * 모든 커스텀 예외의 상위 클래스
 */
@Getter
public class CustomException extends RuntimeException {
  private final ErrorCode errorCode;

  public CustomException(ErrorCode errorCode) {
    super(errorCode.getMessage()); // 메시지는 자동 설정
    this.errorCode = errorCode;
  }
}
