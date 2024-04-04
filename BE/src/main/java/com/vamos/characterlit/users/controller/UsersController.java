package com.vamos.characterlit.users.controller;

import com.vamos.characterlit.auth2.annotation.ExtractPayload;
import com.vamos.characterlit.auth2.security.CustomOAuth2CookieService;
import com.vamos.characterlit.users.domain.UserUpdate;
import com.vamos.characterlit.users.domain.Users;
import com.vamos.characterlit.users.repository.UsersRepository;
import com.vamos.characterlit.users.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService usersService;
    private final UsersRepository usersRepository;
    private final CustomOAuth2CookieService customOAuth2CookieService;

    @GetMapping("/login")
    public ResponseEntity<?> loginUser(HttpServletRequest request) {
        String token = customOAuth2CookieService.getCookie(request);
        if (token == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Users user = usersService.getLoginUser(token);
        return new ResponseEntity<Users>(user, HttpStatus.OK);
    }

    @PatchMapping("/login")
    public ResponseEntity<Users> updateUser(@RequestBody UserUpdate userUpdate) {
        Users updatedUser = usersService.updateUser(userUpdate);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/id")
    public ResponseEntity<?> getUserByNumber(@ExtractPayload long userNumber) {
        Users user = usersRepository.findByUserNumber(userNumber);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Users>(user, HttpStatus.OK);
    }

    @GetMapping("/find/nickname/{nickname}")
    public ResponseEntity<Boolean> isExistNickname(@PathVariable String nickname) {
        boolean isExist = usersRepository.existsUserByNickname(nickname);
        return new ResponseEntity<>(isExist, HttpStatus.OK);
    }
}
