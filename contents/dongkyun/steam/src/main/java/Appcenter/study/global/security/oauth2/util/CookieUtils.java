package Appcenter.study.global.security.oauth2.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.Base64;
import java.util.Optional;

@Slf4j
public class CookieUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    log.debug("Found cookie: name={}", name);
                    return Optional.of(cookie);
                }
            }
        }
        log.debug("Cookie not found: name={}", name);
        return Optional.empty();
    }

    public static void setCookie(HttpServletResponse response, String name, String value, int maxAge) {

        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
        log.debug("Set cookie: name={}, maxAge={}", name, maxAge);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setPath("/");
                    cookie.setValue("");
                    cookie.setMaxAge(0);

                    response.addCookie(cookie);
                    log.debug("Deleted cookie: name={}", name);
                }
            }
        } else {
            log.debug("No cookies found when trying to delete: name={}", name);
        }
    }

    public static String serialize(Object object) {
        try {
            return Base64.getUrlEncoder()
                    .encodeToString(SerializationUtils.serialize((Serializable) object));
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to serialize object to cookie", e);
        }
    }

    public static <T> T deserialize(Cookie cookie, Class<T> clazz) {
        try {
            byte[] bytes = Base64.getUrlDecoder().decode(cookie.getValue());
            return objectMapper.readValue(bytes, clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to deserialize cookie to object", e);
        }
    }
}
