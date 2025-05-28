package Appcenter.study.global.security.oauth2;

import Appcenter.study.global.security.oauth2.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST = "oauth2_authorization_request";
    public static final String REDIRECT_URI_PARAM = "redirect_uri";
    public static final String MODE_PARAM = "mode";
    private static final int COOKIE_EXPIRED_TIME = 100;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        OAuth2AuthorizationRequest authRequest = CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);

        log.debug("Loaded OAuth2AuthorizationRequest from cookie: {}", authRequest != null ? "present" : "not found");

        return authRequest;
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            log.debug("AuthorizationRequest is null, deleting related cookies.");
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST);
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM);
            CookieUtils.deleteCookie(request, response, MODE_PARAM);

            return;
        }

        log.debug("Saving OAuth2AuthorizationRequest to cookie.");
        CookieUtils.setCookie(response, OAUTH2_AUTHORIZATION_REQUEST, CookieUtils.serialize(authorizationRequest), COOKIE_EXPIRED_TIME);

        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM);
        if (StringUtils.hasText(redirectUriAfterLogin)) {
            log.debug("Saving redirect_uri to cookie: {}", redirectUriAfterLogin);
            CookieUtils.setCookie(response, REDIRECT_URI_PARAM, redirectUriAfterLogin, COOKIE_EXPIRED_TIME);
        }

        String mode = request.getParameter(MODE_PARAM);
        if (StringUtils.hasText(mode)) {
            log.debug("Saving mode to cookie: {}", mode);
            CookieUtils.setCookie(response, MODE_PARAM, mode, COOKIE_EXPIRED_TIME);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Removing OAuth2AuthorizationRequest (only from memory, not cookies).");
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Removing all OAuth2-related cookies.");
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM);
        CookieUtils.deleteCookie(request, response, MODE_PARAM);
    }
}
