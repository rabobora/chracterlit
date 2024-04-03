package com.vamos.characterlit.auth2.controller;

import com.vamos.characterlit.auth2.security.CustomOAuth2CookieService;
import com.vamos.characterlit.auth2.service.ReissueService;
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

    private final ReissueService reissueService;
    private final CustomOAuth2CookieService customOAuth2CookieService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        //get refresh token
        Cookie[] cookies = request.getCookies();
        String refresh = reissueService.getRefreshToken(cookies);

        //refresh token 확인
        ResponseEntity<?> res = reissueService.checkRefreshToken(refresh);
        if (!res.getStatusCode().equals(HttpStatus.OK)) {
            return res;
        }

        //response
        String newAccessToken = reissueService.reissueAccessToken(refresh);
        response.setHeader("access_token", newAccessToken);
        response.addCookie(customOAuth2CookieService.createCookie("access_token", newAccessToken, true));
        return res;
    }
}
