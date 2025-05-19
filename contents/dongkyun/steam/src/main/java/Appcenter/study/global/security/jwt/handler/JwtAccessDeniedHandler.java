package Appcenter.study.global.security.jwt.handler;

import Appcenter.study.global.exception.ErrorCode;
import Appcenter.study.global.exception.ErrorResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 403
        response.setStatus(ErrorCode.JWT_ACCESS_DENIED.getHttpStatus().value());
        // 응답의 콘텐츠 타입을 JSON 형식으로 지정
        response.setContentType("application/json;charset=UTF-8");

        // Java 객체를 JSON으로 바꿔주는 도구
        ObjectMapper objectMapper = new ObjectMapper();
        // 아래 객체를 직렬화
        String json = objectMapper.writeValueAsString(
                ErrorResponseEntity.builder()
                        .code(ErrorCode.JWT_ACCESS_DENIED.getCode())
                        .name(ErrorCode.JWT_ACCESS_DENIED.name())
                        .message(ErrorCode.JWT_ACCESS_DENIED.getMessage())
                        .errors(Collections.emptyList())
                        .build()
        );
        // JSON을 HTTP 응답 본문에 작성
        response.getWriter().write(json);
    }
}
