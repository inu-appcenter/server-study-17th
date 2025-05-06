package study.server.global.exception.user;

import study.server.global.exception.ErrorCode;
import study.server.global.exception.CustomException;

/**
 *
 * 이미 등록된 이메일이 있을 때 발생
 */
public class UserAlreadyExistsException extends CustomException {
  public UserAlreadyExistsException() {
    super(ErrorCode.EMAIL_DUPLICATED);
  }
}
