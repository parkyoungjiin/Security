package com.cos.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.security.model.User;
import com.cos.security.repository.UserRepository;

@Service
public class PrincipalDetailsService implements UserDetailsService{

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntity = userRepository.findByUsername(username);
		if(userEntity != null) {
			//객체가 있을 경우 PrincipalDetails를 리턴함.
			return new PrincipalDetails(userEntity); //PrincipalDetails에 user가 컴포지션 되어 있기에, user 오브젝트를 담아서 리턴해야 함.
		}
		return null;
	}
	
}
