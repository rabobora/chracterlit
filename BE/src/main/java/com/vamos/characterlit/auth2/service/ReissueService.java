package com.vamos.characterlit.auth2.service;

import com.vamos.characterlit.auth2.security.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JWTUtil jwtUtil;
    private final com.vamos.characterlit.auth2.security.CustomOAuth2CookieService CustomOAuth2CookieService;
    private final com.vamos.characterlit.auth2.security.CustomOAuth2TokenService CustomOAuth2TokenService;

    public String getRefreshToken(Cookie[] cookies) {
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh_token")) {

                return cookie.getValue();
            }
        }

        return null;
    }

    public ResponseEntity<?> checkRefreshToken(String refresh) {

        if (refresh == null) {

            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {

            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        //DB에 저장되어 있는지 확인
        Boolean isRefreshExist = CustomOAuth2TokenService.isRefreshExist(refresh);

        if (!isRefreshExist) {

            //response body
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    public String[] reissueTokens(String refresh) {
//        String[] newJwts = new String[2];
//        String userId = jwtUtil.getUserId(refresh);
//        String role = jwtUtil.getRole(refresh);
//        long userNumber = jwtUtil.getUserNumber(refresh);
//
//        //make new JWT
//        newJwts[0] = jwtUtil.createJwt("access", userNumber, userId, role, 3600000L);
//        newJwts[1] = jwtUtil.createJwt("refresh", userNumber, userId, role, 172800000L);
//
//        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
//        CustomOAuth2TokenService.deleteRefresh(refresh);
//        CustomOAuth2TokenService.addRefresh(userId, newJwts[1], 172800000L);
//
//        return newJwts;
//    }

    public String reissueAccessToken(String refresh) {
        String userId = jwtUtil.getUserId(refresh);
        String role = jwtUtil.getRole(refresh);
        long userNumber = jwtUtil.getUserNumber(refresh);
        String newAccessToken = jwtUtil.createJwt("access", userNumber, userId, role, 3600000L);
        // 3600000L 4000L
        return newAccessToken;
    }
}
