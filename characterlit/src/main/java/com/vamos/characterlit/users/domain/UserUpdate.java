package com.vamos.characterlit.users.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdate {
    private long userNumber;
    private String email;
    private String name;
    private String nickname;
    private String phoneNumber;
    private String address;
    private String profileImg;
}
