package com.cos.blogbase.config.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{

	private Map<String, Object> attributes; // oauth2User.getAttributes()
	private Map<String, Object> profile; 
	
	public KakaoUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
		this.profile = (Map<String, Object>) attributes.get("profile");
	}
	
	@Override
	public String getProviderId() {
		return String.valueOf(attributes.get("id"));
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getEmail() {
		return (String) attributes.get("email");
	}

	@Override
	public String getName() {
		return (String) profile.get("nickname");
	}
	
}
