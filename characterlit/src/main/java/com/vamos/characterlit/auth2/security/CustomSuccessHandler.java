package com.vamos.characterlit.auth2.security;

import com.vamos.characterlit.auth2.security.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final CustomOAuth2CookieService CustomOAuth2CookieService;
    private final CustomOAuth2TokenService CustomOAuth2TokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        // 유저 정보
        String username = customUserDetails.getUserId();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 토큰 생성
        String accessToken = jwtUtil.createJwt("access", username, role, 6000000L);
        String refreshToken = jwtUtil.createJwt("refresh", username, role, 86400000L);

        //Refresh 토큰 저장
        CustomOAuth2TokenService.addRefresh(username, refreshToken, 86400000L);

        // 응답 설정
        response.setHeader("access_token", accessToken);
        response.addCookie(CustomOAuth2CookieService.createCookie("access_token", accessToken, true));
        response.addCookie(CustomOAuth2CookieService.createCookie("refresh_token", refreshToken, false));
        response.sendRedirect("http://localhost:3000/");
        response.setStatus(HttpStatus.OK.value());
    }

}
