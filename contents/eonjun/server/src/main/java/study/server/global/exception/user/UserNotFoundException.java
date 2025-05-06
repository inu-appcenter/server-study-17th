package study.server.global.exception.user;

import study.server.global.exception.CustomException;
import study.server.global.exception.ErrorCode;

/**
 *
 * 유저가 db에 존재하지 않을 때
 */
public class UserNotFoundException extends CustomException {
  public UserNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND);
  }
}

