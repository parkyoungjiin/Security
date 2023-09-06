package com.cos.security.controller;

import java.security.Principal;

import javax.management.relation.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security.auth.PrincipalDetails;
import com.cos.security.model.User;
import com.cos.security.repository.UserRepository;

@Controller //View 리턴.
public class IndexController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) {
		System.out.println("/test/login ========");
		//User 객체 찾는 방법 1 : PrincipalDetails를 authentication.getPrincipal 후 다운캐스팅하여 User 객체를 저장.
		PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();  
		//User 객체 찾는 방법 2 : @AuthenticationPrincipal 활용하여 principalDetails를 통해 User객체 저장.
		System.out.println("authentication : " + principalDetails.getUser());
		System.out.println("userDetails : " + userDetails.getUser());
		return "세션 정보 확인하기";
	}
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOauthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth) {
		System.out.println("/test/oauth/login ========");
		//User 객체 찾는 방법 1 : PrincipalDetails를 authentication.getPrincipal 후 다운캐스팅하여 User 객체를 저장.
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();  
		//User 객체 찾는 방법 2 : @AuthenticationPrincipal 활용하여 principalDetails를 통해 User객체 저장.
		System.out.println("authentication : " + oAuth2User.getAttributes());
		System.out.println("oAuth2User : " + oAuth.getAttributes());
		
		return "oauth세션 정보 확인하기";
	}
	
	@GetMapping({ "", "/" })
	public String index() {
		//머스테치 템플릿 엔진 사용 (기본 폴더 : src/main/resources/)
		//뷰 리졸버 설정 : templates(prefix), .mustache(suffix) (maven 등록 시 자동으로 되기에 설정 생략 가능)
		System.out.println("index호출");
		return "index"; 
	}
	
	//유저 조회
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails:" + principalDetails.getUser());
		return "user";
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("/join")
	public String joinProc(User user) {
		System.out.println("user : " + user);
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		
		userRepository.save(user);
		return "redirect:/loginForm";
	}
	
	// 특정 메서드에 권한을 지정하고 싶을 때 Config 파일에 @EnableGlobalMethodSecurity(securedEnabled = true)를 한 뒤
	// 설정하고 싶은 메서드에 어노테이션을 설정해주면 된다.
	@Secured("ROLE_ADMIN") 
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	//data라는 메서드가 실행되기 직전에 실행되는 어노테이션. 여러 개를 걸고 싶을 경우 사용
	
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터 정보";
	}
	
}
