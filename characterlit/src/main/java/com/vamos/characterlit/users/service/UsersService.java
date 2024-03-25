package com.vamos.characterlit.users.service;

import com.vamos.characterlit.auth2.security.jwt.JWTUtil;
import com.vamos.characterlit.users.domain.Users;
import com.vamos.characterlit.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final JWTUtil jwtUtil;
    private final UsersRepository usersRepository;

    @Transactional
    public Users getLoginUser(String token) {
        String username = jwtUtil.getUsername(token);
        Users loginUser = usersRepository.findByUsername(username);
        return loginUser;
    }
}
