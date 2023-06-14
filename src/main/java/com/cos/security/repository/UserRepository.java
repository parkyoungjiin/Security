package com.cos.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security.model.User;

//CRUD 함수를 JpaRepository에 있음.
//@Repository 필요가 없음. => JpaRepository를 상속했기에 IoC에 등록된다.
public interface UserRepository extends JpaRepository<User, Integer>{
	//findBy 까지는 규칙, Username은 문법. => findBy*** : ***은 where절에 들어감. (JPA naming)
	public User findByUsername(String username);
}
