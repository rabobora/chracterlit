package com.vamos.characterlit.auth2.security;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2CookieService {

    public Cookie createCookie(String key, String value, boolean isTemp) {

        Cookie cookie = new Cookie(key, value);
        if (!isTemp)
            cookie.setMaxAge(2 * 24 * 60 * 60);
        else
            cookie.setMaxAge(60 * 60);

        // setSecure : Https 옵션이 아니면 쿠키를 전송하지 않음
        // cookie.setSecure(true);

        // setPath : 쿠키를 전송받고자 하는 경로 설정
        cookie.setPath("/");

        // setHttpOnly : 서버에서만 쿠키를 확인할 수 있음 (프론트에서 확인 X)
        if (!isTemp)
            cookie.setHttpOnly(true);
        return cookie;
    }
}
