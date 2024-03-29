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

    @GetMapping("/loginuser")
    public ResponseEntity<?> loginUser(HttpServletRequest request) {
        String token = customOAuth2CookieService.getCookie(request);
        if (token == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Users user = usersService.getLoginUser(token);
        return new ResponseEntity<Users>(user, HttpStatus.OK);
    }

    @PatchMapping("/loginuser")
    public ResponseEntity<Users> updateUser(@RequestBody UserUpdate userUpdate) {
        Users updatedUser = usersService.updateUser(userUpdate);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/id")
    public ResponseEntity<?> getUserByNumber(@ExtractPayload long userNumber) {
        System.out.println("getUserById : " + userNumber);
        Users user = usersRepository.findByUserNumber(userNumber);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Users>(user, HttpStatus.OK);
    }


}
