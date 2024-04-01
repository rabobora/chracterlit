package com.vamos.characterlit.users.service;

import com.vamos.characterlit.auth2.security.jwt.JWTUtil;
import com.vamos.characterlit.users.domain.UserUpdate;
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
        String userId = jwtUtil.getUserId(token);
        Users loginUser = usersRepository.findByUserId(userId);
        return loginUser;
    }

    @Transactional
    public Users updateUser(UserUpdate userUpdate) {
        Users user = usersRepository.findByUserNumber(userUpdate.getUserNumber());
        if(user == null)
            throw new IllegalArgumentException("can't find user : " + user.getUserNumber());

        // 유저 정보 업데이트
        if(userUpdate.getEmail() != null) user.setEmail(userUpdate.getEmail());
        if(userUpdate.getName() != null) user.setName(userUpdate.getName());
        if(userUpdate.getNickname() != null) user.setNickname(userUpdate.getNickname());
        if(userUpdate.getPhoneNumber() != null) user.setPhoneNumber(userUpdate.getPhoneNumber());
        if(userUpdate.getAddress() != null) user.setAddress(userUpdate.getAddress());
        if(userUpdate.getProfileImg() != null) user.setProfileImg(userUpdate.getProfileImg());

        return user;
    }
}
