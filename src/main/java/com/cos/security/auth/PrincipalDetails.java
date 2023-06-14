package com.cos.security.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cos.security.model.User;

public class PrincipalDetails implements UserDetails{
	//User 정보를 PrincipalDetails에 저장해야 하기에, 컴포지션을 사용.
	// ** 컴포지션 ** : 기존 클래스가 새로운 클래스의 구성요소가 되는 것을 말한다.
	private User user;
	
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	//유저의 권한 리턴.
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}
	//유저의 패스워드 리턴
	@Override
	public String getPassword() {
		return user.getPassword();
	}
	//유저의 이름 리턴
	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() { // ex ) login 1년 지난회원 휴먼계정 전환 시 사용 가능!
		return true;
	}
	
}
