package com.cos.blogbase.model;


import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// @DynamicInsert // insert 할 때 null인 필드 제외
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//ORM -> Java(다른언어) Object -> 테이블로 매핑해주는 기술
@Entity // User 클래스가 MySQL에 테이블이 생성된다.
public class User {
	
	@Id // primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
	private int id; // 시퀀스, auto_increment
	
	@Column(nullable = false, length = 100, unique = true) // 아이디가 null 값이 될 수 없고 길이 제한을 30자로 한다.
	private String username; // 아이디
	
	@Column(nullable = false, length = 100) // 123456 => 해쉬 (비밀번호 암호화)
	private String password;
	
	@Column(nullable = false, length = 50)
	private String email;
	
	// @ColumnDefault("'user'")
	// DB는 RoleType이란 게 없다.
	@Enumerated(EnumType.STRING)
	private RoleType role; // Enum을 쓰는 게 좋다. // ADMIN, USER
	
	private String oauth; // kakao, google
	private String oauthId;
	
	@CreationTimestamp // 시간이 자동 입력
	private Timestamp createDate; // 가입한 시간

	@Builder
	public User(String username, String password, String email, RoleType role, String oauth, String oauthId,
			Timestamp createDate) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
		this.oauth = oauth;
		this.oauthId = oauthId;
		this.createDate = createDate;
	}

	
}
