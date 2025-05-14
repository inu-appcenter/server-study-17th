package Appcenter.study.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // jwt Exception
    JWT_NOT_VALID(HttpStatus.UNAUTHORIZED, 401, "[Jwt] 유효하지 않은 Jwt"),
    JWT_ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, 419, "[Jwt] 만료된 엑세스 토큰입니다."),
    JWT_REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, 420, "[Jwt] 만료된 리프레시 토큰입니다."),
    JWT_MALFORMED(HttpStatus.UNAUTHORIZED, 401, "[Jwt] 잘못된 토큰 형식입니다."),
    JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, 401, "[Jwt] 유효하지 않은 서명입니다."),
    JWT_UNSUPPORTED(HttpStatus.UNAUTHORIZED, 401, "[Jwt] 지원하지 않는 토큰입니다."),
    JWT_ENTRY_POINT(HttpStatus.UNAUTHORIZED, 401, "[Jwt] 인증되지 않은 사용자입니다."),
    JWT_ACCESS_DENIED(HttpStatus.FORBIDDEN, 403, "[Jwt] 리소스에 접근할 권한이 없습니다."),

    // Validation
    VALIDATION_FAILED(HttpStatus.BAD_GATEWAY, 400, "요청한 값이 올바르지 않습니다."),

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "맴버를 찾을 수 없습니다."),
    LOGIN_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "존재하지 않는 이메일입니다."),
    PASSWORD_NOT_CORRECT(HttpStatus.BAD_REQUEST, 400, "비밀번호가 일치하지 않습니다."),
    EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, 400, "이메일이 중복되었습니다."),
    NICKNAME_DUPLICATED(HttpStatus.BAD_REQUEST, 400, "닉네임이 중복되었습니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, 401, "로그인이 실패하였습니다."),

    // Game
    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "게임을 찾을 수 없습니다."),

    // Purchase
    PURCHASE_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "구매 정보를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;
}
