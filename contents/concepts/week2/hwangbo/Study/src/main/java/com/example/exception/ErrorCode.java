package com.example.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "존재하지 않는 사용자입니다."),
    EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, 400, "중복된 이메일입니다."),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "존재하지 않는 상품입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, 400, "잘못된 입력입니다."),
    PASSWORD_NOT_CORRECT(HttpStatus.UNAUTHORIZED, 401, "비밀번호가 일치하지 않습니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, 400, "비밀번호 확인 값이 일치하지 않습니다.");


    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;
}
