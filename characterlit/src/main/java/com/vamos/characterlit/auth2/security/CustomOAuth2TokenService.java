package com.vamos.characterlit.auth2.security;

import com.vamos.characterlit.auth2.domain.Refresh;
import com.vamos.characterlit.auth2.repository.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class CustomOAuth2TokenService {
    private final RefreshRepository refreshRepository;

    public void addRefresh(String username, String refreshToken, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh newRefreshToken = new Refresh();
        newRefreshToken.setUsername(username);
        newRefreshToken.setRefresh(refreshToken);
        newRefreshToken.setExpiration(date.toString());

        refreshRepository.save(newRefreshToken);
    }

    public void deleteRefresh(String refreshToken) {
        refreshRepository.deleteByRefresh(refreshToken);
    }

    public boolean isRefreshExist(String refreshToken) {
        return refreshRepository.existsByRefresh(refreshToken);
    }
}
