package Appcenter.study.global.security.oauth2.handler;

import Appcenter.study.domain.member.dto.res.LoginResponseDto;
import Appcenter.study.global.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import Appcenter.study.global.security.oauth2.service.CustomOAuth2UserService;
import Appcenter.study.global.security.oauth2.service.OAuth2UserPrincipal;
import Appcenter.study.global.security.oauth2.user.OAuth2UserUnlinkManager;
import Appcenter.study.global.security.oauth2.util.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

import static Appcenter.study.global.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.MODE_PARAM;
import static Appcenter.study.global.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;

    // OAUTH 인증이 성공적으로 완료되었을 때 호출
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException {

        log.debug("OAuth2 authentication success. Starting post-auth processing.");

        // 인증 성공 후 사용자를 리디렉션할 URL 설정
        String targetUrl = setTargetUrl(request, response, authentication);

        log.debug("Redirecting user to: {}", targetUrl);

        // 인증 관련 쿠키 및 속성 제거
        clearAuthenticationAttributes(request, response);

        // 설정된 URL로 사용자 리디렉션
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    // 인증 성공 후 리디렉션할 URL을 생성하는 메서드
    protected String setTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        // 프론트에서 보낸 redirect_uri 값을 쿠키에서 읽어옴
        Optional<String> redirectUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM)
                .map(Cookie::getValue);

        // 기본 리디렉션 URL 또는 쿠키에서 가져온 redirect URL
        String targetUrl = redirectUrl.orElse(getDefaultTargetUrl());

        // mode (login, unlink 등) 쿠키에서 추출
        String mode = CookieUtils.getCookie(request, MODE_PARAM)
                .map(Cookie::getValue)
                .orElse("");

        log.debug("OAuth2 redirect URL from cookie: {}", targetUrl);
        log.debug("OAuth2 mode from cookie: {}", mode);

        // 인증된 사용자 정보 (OAuth2UserPrincipal) 가져오기
        OAuth2UserPrincipal userPrincipal = getOAuth2UserDetails(authentication);

        // 사용자 정보가 없으면 에러 리디렉션
        if (userPrincipal == null) {
            log.warn("OAuth2UserPrincipal is null. Redirecting with error.");
            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("error", "Login Failed with Authentication Error")
                    .build().toUriString();
        }

        log.debug("OAuth2 user principal info: provider={}, name={}",
                userPrincipal.getUserInfo().getProvider(),
                userPrincipal.getName());

        // login 모드일 경우: 회원가입 여부 판단
        if ("login".equalsIgnoreCase(mode)) {
            // 사용자가 회원가입되어 있지 않으면 회원가입 페이지로 리디렉션
            if (!customOAuth2UserService.checkUserPresent(userPrincipal.getName())) {
                log.info("User not registered. Redirecting to signup.");
                return UriComponentsBuilder.fromUriString(targetUrl)
                        // 프론트에서 만든 회원가입 페이지 (예: localhost:3000/signup)
                        .path("/signup")
                        .queryParam("provider", userPrincipal.getUserInfo().getProvider())
                        .build().toUriString();
            } else {
                // 이미 회원이면 JWT 토큰 발급 후 메인 페이지로 리디렉션
                log.info("User already registered. Logging in.");
                LoginResponseDto loginResponseDto = customOAuth2UserService.oAuth2Login(authentication);

                log.debug("Access token generated: {}", loginResponseDto.getAccessToken());

                return UriComponentsBuilder.fromUriString(targetUrl)
                        .path("/main")
                        .queryParam("accessToken", loginResponseDto.getAccessToken())
                        .build().toUriString();
            }
        }
        // unlink 모드일 경우: 연결 해제 처리 후 리디렉션
        else if ("unlink".equalsIgnoreCase(mode)) {
            log.info("Unlink mode detected. Unlinking user from provider.");
            oAuth2UserUnlinkManager.unlink(userPrincipal.getUserInfo().getProvider(), userPrincipal.getUserInfo().getAccessToken());

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .build().toUriString();
        }

        // 예외 상황 시 에러 리디렉션
        log.warn("Unexpected mode value: {}. Redirecting with error.", mode);
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "Login Failed with unexpected Error")
                .build().toUriString();
    }

    // Authentication 객체에서 OAuth2UserPrincipal을 추출
    private OAuth2UserPrincipal getOAuth2UserDetails(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        }

        log.warn("Unexpected principal type: {}", principal.getClass().getName());
        return null;
    }

    // 인증 과정에서 생성된 쿠키 및 세션 속성 제거
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        log.debug("Cleared authentication cookies and session attributes.");
    }
}
