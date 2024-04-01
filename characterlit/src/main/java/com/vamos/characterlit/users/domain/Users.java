package com.vamos.characterlit.users.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Setter
@Getter
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userNumber;

    private int loginServer;

    private String userId;

    private String role;

    private String email;

    private String name;

    private String nickname;

    private String phoneNumber;

    private String address;

    private String profileImg;

    private int credit;

    private Timestamp createdDate;

    private Timestamp deletedDate;

}
