package Appcenter.study.global.security.oauth2;

import Appcenter.study.global.security.oauth2.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST = "oauth2_authorization_request";
    public static final String REDIRECT_URI_PARAM = "redirect_uri";
    public static final String MODE_PARAM = "mode";
    private static final int COOKIE_EXPIRED_TIME = 100;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST);
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM);
            CookieUtils.deleteCookie(request, response, MODE_PARAM);

            return;
        }

        CookieUtils.setCookie(response, OAUTH2_AUTHORIZATION_REQUEST, CookieUtils.serialize(authorizationRequest), COOKIE_EXPIRED_TIME);

        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM);

        if (StringUtils.hasText(redirectUriAfterLogin)) {
            CookieUtils.setCookie(response, REDIRECT_URI_PARAM, redirectUriAfterLogin, COOKIE_EXPIRED_TIME);
        }

        String mode = request.getParameter(MODE_PARAM);

        if (StringUtils.hasText(mode)) {
            CookieUtils.setCookie(response, MODE_PARAM, mode, COOKIE_EXPIRED_TIME);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM);
        CookieUtils.deleteCookie(request, response, MODE_PARAM);
    }
}
