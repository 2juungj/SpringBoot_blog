package com.cos.blogbase.config.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.blogbase.config.auth.PrincipalDetail;
import com.cos.blogbase.config.oauth.provider.FacebookUserInfo;
import com.cos.blogbase.config.oauth.provider.GoogleUserInfo;
import com.cos.blogbase.config.oauth.provider.KakaoUserInfo;
import com.cos.blogbase.config.oauth.provider.NaverUserInfo;
import com.cos.blogbase.config.oauth.provider.OAuth2UserInfo;
import com.cos.blogbase.model.RoleType;
import com.cos.blogbase.model.User;
import com.cos.blogbase.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	// 구글로부터 받은 userRequest 데이터에 대해 후처리되는 함수
	// 함수 종료 시 @AuthenticationPrincipal 어노테이션이 생성된다.
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("getClientRegistration : " + userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인 했는지 확인가능.
		System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());

		OAuth2User oauth2User = super.loadUser(userRequest);
		// 구글로그인 버튼 클릭 -> 구글 로그인 페이지 -> 로그인 완료 -> code를 리턴(OAuth-Client 라이브러리가 처리) -> AccessToken 요청 (여기까지가 userRequest 정보)
		// userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원 프로필 받아준다.
		System.out.println("getAttributes : " + oauth2User.getAttributes());
		
		// 강제 회원가입
		OAuth2UserInfo oAuth2UserInfo = null;
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
		}else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
			System.out.println("페이스북 로그인 요청");
			oAuth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes());
		}else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			System.out.println("네이버 로그인 요청");
			oAuth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));
		}else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
			System.out.println("카카오 로그인 요청");
			oAuth2UserInfo = new KakaoUserInfo((Map)oauth2User.getAttributes().get("kakao_account"));
		}else {
			System.out.println("지원하지 않는 OAuth 로그인입니다.");
		}
		
		String oauth = oAuth2UserInfo.getProvider(); // google
		String oauthId = oAuth2UserInfo.getProviderId();
		String username = oauth + "_" + oauthId; // google_{sub값} or facebook_{id값}
		String password = bCryptPasswordEncoder.encode("겟인데어");
		String email = oAuth2UserInfo.getEmail();
		
		User userEntity = userRepository.findByUsername(username)
                .orElseGet(() -> {
                	System.out.println("가입되지 않은 계정입니다. 가입을 진행합니다.");
                    User newUser = User.builder() // 람다 표현식 내에서 외부 변수 수정하려면 final이나 effectively final 이여야 한다.
                            .username(username)		// 아니면 지금처럼 람다 표현식 내에서 새로운 변수를 생성해야 한다.
                            .password(password)
                            .email(email)
                            .role(RoleType.USER)
                            .oauth(oauth)
                            .oauthId(oauthId)
                            .build();
                    System.out.println("가입이 완료되었습니다.");
                    return userRepository.save(newUser);
                });
		
		return new PrincipalDetail(userEntity, oauth2User.getAttributes());
	}
}
