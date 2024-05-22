package com.cos.blogbase.config;

import org.springframework.beans.factory.annotation.Autowired;

// 1. 코드를 받아서 인증
// 2. 액세스 토큰을 받아서 권한 받기
// 3. 사용자 프로필 정보를 가져오기
// 4-1. 해당 정보를 기반으로 회원가입을 자동으로 진행
// 4-2. 구글에 등록된 정보 외에 필요한 정보가 있을 때 추가적으로 데이터 관리 (쇼핑몰 -> 주소, 회원등급)

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import com.cos.blogbase.config.oauth.PrincipalOauth2UserService;

// Bean 등록: 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것 (클래스에 @Configuration, 메서드에 @Bean)
@Configuration // IoC
public class SecurityConfig {
	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
	// 로그인에 필요한 정보(username, password)를 DB와 비교하여 인증해준다.
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.csrf().disable(); // csrf 토큰 비활성화 (테스트 시 걸어두는 게 좋다.)
		http.authorizeHttpRequests(
				auth -> auth.requestMatchers("/","/auth/**", "/WEB-INF/**","js/**","/css/**","image/**","/dummy/**")
				.permitAll()
				.requestMatchers(RegexRequestMatcher.regexMatcher("/board/\\d+")).permitAll()
				//.requestMatchers("/board/**").hasRole("ADMIN")
				.anyRequest()
				.authenticated());
		http.formLogin(f -> f.loginPage("/auth/loginForm")
				.permitAll()
				.loginProcessingUrl("/auth/loginProc") // 스프링 시큐리티가 해당 주소로 요청오는 로그인을 가로채서 대신 로그인 해준다.
				.defaultSuccessUrl("/")); // 로그인이 끝나면 해당 주소로 이동한다.
	    http.oauth2Login(oauth2 -> oauth2
                .loginPage("/auth/loginForm") // 구글 로그인이 완료되고 후처리가 필요
                														// Tip. OAuth 클라이언트 라이브러리를 사용하면 코드X, (액세스 토큰 + 사용자 프로필 정보 O)
                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                .userService(principalOauth2UserService)));
		
		return http.build();
	}
}
