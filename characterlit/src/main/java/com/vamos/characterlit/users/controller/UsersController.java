package com.vamos.characterlit.users.controller;

import com.vamos.characterlit.auth2.security.CustomOAuth2CookieService;
import com.vamos.characterlit.auth2.security.CustomOAuth2TokenService;
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

    @GetMapping("/{userNumber}")
    public ResponseEntity<?> getUserByNumber(@PathVariable("userNumber") Long userNumber) {
        Users user = usersRepository.findByUserNumber(userNumber);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Users>(user, HttpStatus.OK);
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") String userId) {
        Users user = usersRepository.findByUserId(userId);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Users>(user, HttpStatus.OK);
    }
}
