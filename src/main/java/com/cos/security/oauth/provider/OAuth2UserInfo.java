package com.cos.security.oauth.provider;

public interface OAuth2UserInfo { // OAuth 로그인 형태별로 다르기에 ProviderId, Provider, Email, Name을 받는 인터페이스 생성.
	
	String getProviderId();
	String getProvider();
	String getEmail();
	String getName();
	
}
