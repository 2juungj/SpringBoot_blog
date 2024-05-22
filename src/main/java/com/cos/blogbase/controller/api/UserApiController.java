package com.cos.blogbase.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blogbase.config.auth.PrincipalDetail;
import com.cos.blogbase.dto.ResponseDto;
import com.cos.blogbase.model.User;
import com.cos.blogbase.service.UserService;


@RestController
public class UserApiController {

	@Autowired
	private UserService userService;

	@PostMapping("/auth/joinProc")
	public ResponseDto<Integer> save(@RequestBody User user) { // username, password, email
		System.out.println("UserApiController: save 호출됨");
		// 실제로 DB에 insert하고 아래에서 return이 되면 된다.
		userService.회원가입(user);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); // 자바오브젝트를 json으로 변환해서 리턴 (Jackson)
	}
	
	@PutMapping("/user")
	public ResponseDto<Integer> update(@RequestBody User user, @AuthenticationPrincipal PrincipalDetail principal){
		userService.회원수정(user,principal);
		// 여기서 트랜잭션이 종료되기 때문에 DB의 값은 변경됐다.
		// 그러나 세션의 값은 변경되지 않은 상태이기 때문에 직접 세션 값을 변경해준다.
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
}
