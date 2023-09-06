package com.cos.security.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.security.auth.PrincipalDetails;
import com.cos.security.model.User;
import com.cos.security.oauth.provider.GoogleUserInfo;
import com.cos.security.oauth.provider.NaverUserInfo;
import com.cos.security.oauth.provider.OAuth2UserInfo;
import com.cos.security.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	//로그인 후 처리를 담당하는 메서드. (구글로부터 받은 userRequest에 대한 데이터 후처리)
	//함수 종료 시 @AuthenticationPrincipal 어노테이션이 만들어진다.
 
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		System.out.println("getClientRegistration:" + userRequest.getClientRegistration());
		System.out.println("token:" + userRequest.getAccessToken().getTokenValue());
		System.out.println(" loadUser:" + super.loadUser(userRequest).getAttributes());
		
		//OAuth 로그인 후 attributes 정보를 갖고 있음. 
		OAuth2User oauth2User = super.loadUser(userRequest);
		
		System.out.println("getAttributes:" + oauth2User.getAttributes());
		
		// 인터페이스 변수 생성
		OAuth2UserInfo oAuth2UserInfo = null;
		
		if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes()); //attribute가 GoogleUserInfo로 들어가서 get 함수를 만나서 값을 세팅함.
		}else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
			System.out.println("페이스북 로그인 요청");
		}else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			System.out.println("네이버 로그인 요청");
			oAuth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));
		}else {
			System.out.println("구글, 페이스북, 네이버 로그인만 지원");
		}
		// 정보 강제로 추출(강제 로그인을 위한 변수)
		String provider = oAuth2UserInfo.getProvider();
		String providerId = oAuth2UserInfo.getProviderId();
		String username = provider + "_" + providerId;
		String password = bCryptPasswordEncoder.encode("getinthere");
		String email = oauth2User.getAttribute("email");
		String role = "ROLE_USER";
		
		//findByUsername 메서드를 통해 username에 맞는 user엔티티가 DB에 있는 지 확인한다.
		User userEntity = userRepository.findByUsername(username);
		
		// DB에 없는 경우 Entity 객체가 null => 회원가입 강제 진행
		if(userEntity == null) {
			System.out.println("Oauth 회원가입을 진행합니다.");
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		// DB에 있는 경우 Entity 객체가 null => 회원가입 진행 X, 로그인 진행 
		}else {
			System.out.println("구글 로그인을 이미한 적이 있다.");
		}
		
		//Details객체를 생성하여 로그인을 진행함.
		return new PrincipalDetails(userEntity, oauth2User.getAttributes());
	}
}
