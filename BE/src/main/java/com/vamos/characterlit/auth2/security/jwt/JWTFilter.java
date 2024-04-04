package com.vamos.characterlit.auth2.security.jwt;

import com.vamos.characterlit.auth2.response.CustomUserDetails;
import com.vamos.characterlit.auth2.security.CustomOAuth2CookieService;
import com.vamos.characterlit.auth2.security.CustomOAuth2TokenService;
import com.vamos.characterlit.auth2.service.ReissueService;
import com.vamos.characterlit.users.domain.Users;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final ReissueService reissueService;
    private final CustomOAuth2CookieService customOAuth2CookieService;
    private final CustomOAuth2TokenService customOAuth2TokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("access_token");
        String refreshToken = null;
        Cookie[] cookieList = request.getCookies();
        if (cookieList != null) {
            for (Cookie cookie : cookieList) {
                if (cookie.getName().equals("refresh_token")) {
                    refreshToken = cookie.getValue();
                }
            }
        }
        //DB에 RefreshToken이 저장되어 있는지 확인
        Boolean isRefreshExist = customOAuth2TokenService.isRefreshExist(refreshToken);

        // 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {

            filterChain.doFilter(request, response);

            return;
        }

        // RefreshToken가 유효하지 않은 토큰인지 확인
        if (!isRefreshExist) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            //token 재발급
            if (!reissueService.checkRefreshToken(refreshToken).getStatusCode().equals(HttpStatus.OK)) {
                //response status code
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String newAccessToken = reissueService.reissueAccessToken(refreshToken);
            response.setHeader("access_token", newAccessToken);
            response.addCookie(customOAuth2CookieService.createCookie("access_token", newAccessToken, true));
            accessToken = newAccessToken;
            System.out.println("access token expired. reissued.");
            return;
        }

        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // username, role 값을 획득
        String userId = jwtUtil.getUserId(accessToken);
        String role = jwtUtil.getRole(accessToken);

        Users user = new Users();
        user.setUserId(userId);
        user.setRole(role);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
