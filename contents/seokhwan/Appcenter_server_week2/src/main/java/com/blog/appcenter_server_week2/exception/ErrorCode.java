package com.blog.appcenter_server_week2.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    UNAUTHORIZED_LOGIN(UNAUTHORIZED,"아이디 혹은 비밀번호를 잘못입력했습니다."),
    NOT_EXIST_ID(NOT_FOUND,"존재하지 않는 회원입니다."),
    DUPLICATE_LOGINID(CONFLICT,"이미 존재하는 아이디입니다."),
    VALIDATION_ERROR(BAD_REQUEST, "유효성 검사 실패"),
    DATABASE_ERROR(INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;

}
