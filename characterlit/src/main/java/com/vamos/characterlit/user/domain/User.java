package com.vamos.characterlit.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
public class User {
	
	@Id
	@Column(name = "user_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;
	
	@Column(name = "login_server", nullable = false)
	private int loginServer;
	
	@Column(name = "naver_id", unique = true)
	private long naverId;
	
	@Column(name = "kakao_id", unique = true)
	private long kakaoId;
	
	@Column(name = "role", length = 16, nullable = false)
	private String role;
	
	@Column(name = "email", unique = true, nullable = false)
	private String email;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "nickname", unique = true, nullable = false)
	private String nickname;
	
	@Column(name = "phone_number", unique = true, nullable = false)
	private String phoneNumber;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "profile_img")
	private String profileImg;
	
	@Column(name = "credit", nullable = false)
	private int credit;
	
	@Column(name = "created_date", nullable = false)
	private Timestamp createdDate;
	
	@Column(name = "deleted_date")
	private Timestamp deletedDate;
}
