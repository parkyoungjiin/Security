package com.cos.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.cos.security.oauth.PrincipalOauth2UserService;

import lombok.AllArgsConstructor;


@Configuration // IoC 빈(bean)을 등록
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // Secured 어노테이션 활성화
@AllArgsConstructor
public class SecurityConfig {
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;

	@Autowired
    BCryptPasswordEncoder encoder;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
				.antMatchers("/user/**").authenticated() // authenticated : /user의 주소는 인증되야만 한다.
				// .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') or
				// hasRole('ROLE_USER')")
				// .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') and
				// hasRole('ROLE_USER')")
				.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") // access : /admin/* 주소는 인증 + Role을 가져야 함.
				.anyRequest().permitAll() //admin, user를 제외한 주소는 허용 된다.
				.and()
				.formLogin()
				.loginPage("/loginForm")
				.loginProcessingUrl("/login")// login 주소가 호출되면 시큐리티가 낚아채서 로그인 진행함.
				.defaultSuccessUrl("/")
				.and()
				.oauth2Login()
				.loginPage("/loginForm")
				.userInfoEndpoint()
				.userService(principalOauth2UserService);

		return http.build();
	}
}