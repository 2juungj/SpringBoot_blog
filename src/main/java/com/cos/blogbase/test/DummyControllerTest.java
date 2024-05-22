package com.cos.blogbase.test;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blogbase.model.RoleType;
import com.cos.blogbase.model.User;
import com.cos.blogbase.repository.UserRepository;

import jakarta.transaction.Transactional;

// html 파일이 아니라 data를 리턴해주는 controller
@RestController
public class DummyControllerTest {
	
	@Autowired // 의존성 주입(DI)
	private UserRepository userRepository;
	
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			return "삭제 실패. 해당 id는 DB에 없음.";
		}
		
		return "삭제 완료. id : " +id;
	}
	
	// save 함수는 id를 전달하지 않으면 insert
	// save 함수는 id를 전달했을 때, 해당 id에 대한 데이터가 있으면 update
	// save 함수는 id를 전달했을 때, 해당 id에 대한 데이터가 없으면 insert
	// email, password
	@Transactional // 함수 종료 시 자동 commit 됨.
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) { // json 데이터를 요청 => Java Object로 변환해서 받아줌.(Message Converter의 Jackson 라이브러리가 역할 수행)
		System.out.println("id : " + id);
		System.out.println("password : " + requestUser.getPassword());
		System.out.println("email : " + requestUser.getEmail());
		
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정 실패.");
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
		// userRepository.save(user);
		
		// 더티 체킹
		return user;
	}
	
	// http://localhost:8000/blog/dummy/user
	@GetMapping("/dummy/users")
	public List<User> list(){
		return userRepository.findAll();
	}
	
	// 한 페이지당 2건의 데이터를 리턴받아 본다.
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
		org.springframework.data.domain.Page<User> pagingUser = userRepository.findAll(pageable);
		
		List<User> users = pagingUser.getContent();
		return users;
	}
	
	// {id} 주소로 파라미터를 전달 받을 수 있다.
	// http://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		
		// findById가 return하는 타입은 User가 아닌 Optional
		// user/4가 없는데 user/4를 찾으면 user가 null이 된다.
		// 그런 경우 null이 return되고 프로그램에 문제가 발생한다.
		// Optional로 User 객체를 감싸서 가져오므로 null인지 아닌지를 판단 후 return한다.
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				// TODO Auto-generated method stub
				return new IllegalArgumentException("해당 유저 없음. id: "+id);
			}
		});
		// 요청: 웹 브라우저
		// user 객체 = 자바 오브젝트
		// 변환 (웹 브라우저가 이해할 수 있는 데이터로) -> json (Gson 라이브러리)
		// 스프링부트 = MessageConverter가 응답 시에 자동 작동
		// 만약 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson 라이브러리를 호출하여
		// user 오브젝트를 json으로 변환해서 브라우저에게 던져준다.
		return user;
	}

	// http://localhost:8000/blog/dummy/join (요청)
	// http의 body에 username, password, email 데이터를 가지고 (요청)
	@PostMapping("/dummy/join")
	public String join(User user) { // key = value (약속된 규칙)
		System.out.println("id : " + user.getId());
		System.out.println("username : " + user.getUsername());
		System.out.println("password : " + user.getPassword());
		System.out.println("email : " + user.getEmail());
		System.out.println("role : " + user.getRole());
		System.out.println("createDate : " + user.getCreateDate());
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입 완료";
	}
}
