package Appcenter.study.global.security.jwt.handler;

import Appcenter.study.global.exception.ErrorCode;
import Appcenter.study.global.exception.ErrorResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // 403
        response.setStatus(ErrorCode.JWT_ENTRY_POINT.getHttpStatus().value());
        // 응답의 콘텐츠 타입을 JSON 형식으로 지정
        response.setContentType("application/json;charset=UTF-8");

        // Java 객체를 JSON으로 바꿔주는 도구
        ObjectMapper objectMapper = new ObjectMapper();
        // 아래 객체를 직렬화
        String json = objectMapper.writeValueAsString(
                ErrorResponseEntity.builder()
                        .code(ErrorCode.JWT_ENTRY_POINT.getCode())
                        .name(ErrorCode.JWT_ENTRY_POINT.name())
                        .message(ErrorCode.JWT_ENTRY_POINT.getMessage())
                        .build()
        );
        // JSON을 HTTP 응답 본문에 작성
        response.getWriter().write(json);
    }
}
