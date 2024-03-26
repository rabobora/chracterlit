package com.vamos.characterlit.auth2.controller;

import com.vamos.characterlit.auth2.security.CustomOAuth2CookieService;
import com.vamos.characterlit.auth2.security.CustomOAuth2TokenService;
import com.vamos.characterlit.auth2.security.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final CustomOAuth2CookieService CustomOAuth2CookieService;
    private final CustomOAuth2TokenService CustomOAuth2TokenService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh_token")) {

                refresh = cookie.getValue();
            }
        }

        System.out.println(1 + " " + refresh);
        if (refresh == null) {

            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        System.out.println(2);
        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        System.out.println(3);
        if (!category.equals("refresh")) {

            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        //DB에 저장되어 있는지 확인
        Boolean isRefreshExist = CustomOAuth2TokenService.isRefreshExist(refresh);
        System.out.println(4);
        if (!isRefreshExist) {

            //response body
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }
        System.out.println(5);
        String userId = jwtUtil.getUserId(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccessToken = jwtUtil.createJwt("access", userId, role, 6000000L);
        String newRefreshToken = jwtUtil.createJwt("refresh", userId, role, 86400000L);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        CustomOAuth2TokenService.deleteRefresh(refresh);
        CustomOAuth2TokenService.addRefresh(userId, newRefreshToken, 86400000L);

        //response
        response.setHeader("access_token", newAccessToken);
        response.addCookie(CustomOAuth2CookieService.createCookie("access_token", newAccessToken, true));
        response.addCookie(CustomOAuth2CookieService.createCookie("refresh_token", newRefreshToken, false));
        System.out.println("ReissueController : " + newAccessToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
