package com.vamos.characterlit.auth2.security;

import com.vamos.characterlit.auth2.domain.Refresh;
import com.vamos.characterlit.auth2.repository.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CustomOAuth2TokenService {
    private final RefreshRepository refreshRepository;

    public void addRefresh(String userId, String refreshToken, Long expiredMs) {

        deleteAllByUserId(userId);
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        Refresh newRefreshToken = new Refresh();
        newRefreshToken.setUserId(userId);
        newRefreshToken.setRefresh(refreshToken);
        newRefreshToken.setExpiration(date.toString());
        refreshRepository.save(newRefreshToken);
    }

    public void deleteAllByUserId(String userId) {

        refreshRepository.deleteAllByUserId(userId);
    }

    public void deleteRefresh(String refreshToken) {

        refreshRepository.deleteByRefresh(refreshToken);
    }

    public boolean isRefreshExist(String refreshToken) {

        return refreshRepository.existsByRefresh(refreshToken);
    }
}
