package com.kantakap.auction.utils;

import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;
import java.util.Optional;

public class CookieUtils {

    public static Optional<HttpCookie> getCookie(ServerHttpRequest request, String name) {
        var cookies = request.getCookies().get(name);

        if (cookies != null && !cookies.isEmpty()) {
            for (HttpCookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }

        return Optional.empty();
    }

    public static void addCookie(ServerHttpResponse response, String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .httpOnly(true)
                .maxAge(maxAge)
                .build();
        response.addCookie(cookie);
    }

    public static void deleteCookie(ServerHttpRequest request, ServerHttpResponse response, String name) {
        var cookies = request.getCookies().get(name);
        if (cookies != null && !cookies.isEmpty()) {
            for (HttpCookie cookie: cookies) {
                if (cookie.getName().equals(name)) {
                    ResponseCookie responseCookie = ResponseCookie.from(name, cookie.getValue())
                            .path("/")
                            .value("")
                            .httpOnly(true)
                            .maxAge(0)
                            .build();
                    response.addCookie(responseCookie);
                }
            }
        }
    }

    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    public static <T> T deserialize(HttpCookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }


}
