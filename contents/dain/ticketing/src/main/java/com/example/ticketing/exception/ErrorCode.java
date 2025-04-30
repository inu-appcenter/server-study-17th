package com.example.ticketing.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATE_LOGIN_ID(CONFLICT, "USER-001", "이미 존재하는 ID입니다."),
    UNAUTHORIZED_LOGIN(UNAUTHORIZED, "USER-002", "ID 혹은 비밀번호가 일치하지 않습니다."),
    ID_NOT_FOUND(NOT_FOUND, "USER-003", "존재하지 않는 ID입니다."),
    FORBIDDEN_PERMISSION(FORBIDDEN, "USER-004", "권한이 없습니다."),
    SHOW_NOT_FOUND(NOT_FOUND, "SHOW_001", "존재하지 않는 공연입니다."),
    TICKET_NOT_FOUND(NOT_FOUND, "TICKET_001", "존재하지 않는 티켓입니다.");

    private final HttpStatus httpStatus;
    private final String code;				// USER-001
    private final String message;			// 설명
}