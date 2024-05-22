package com.cos.blogbase.test;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// @Getter
// @Setter
@Data	// Getter와 Setter를 동시에
// @AllArgsConstructor	// 모든 필드 값을 파라미터로 받는 생성자를 생성
@NoArgsConstructor	// 파라미터가 없는 디폴트 생성자를 생성
public class Member {
	private int id;
	private String username;
	private String password;
	private String email;
	
	@Builder
	public Member(int id, String username, String password, String email) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
	}

	
}
