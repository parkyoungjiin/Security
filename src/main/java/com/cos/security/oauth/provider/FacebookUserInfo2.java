package com.cos.security.oauth.provider;

import java.util.Map;

public class FacebookUserInfo2 implements OAuth2UserInfo{
	
	private Map<String, Object> attributes; // PrincipalOauthUserService 클래스의 oauth2User의 getAttributes()를 받음.
	
	public FacebookUserInfo2(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public String getProviderId() {
		return (String)attributes.get("id");
	}

	@Override
	public String getProvider() {
		return "facebook";
	}

	@Override
	public String getEmail() {
		return (String)attributes.get("email");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return (String)attributes.get("name");
	}
	
}
