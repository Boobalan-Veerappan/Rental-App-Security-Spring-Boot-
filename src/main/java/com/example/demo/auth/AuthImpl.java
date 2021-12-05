package com.example.demo.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.UserDetailsDomain;
import com.example.demo.dto.LoginDto;
import com.example.demo.dto.UserToken;
import com.example.demo.exception.JwtAuthenticationException;
import com.example.demo.jwtutil.JwtTokenCreator;
import com.example.demo.repo.UserDetailsRepo;

@Service
public class AuthImpl implements AuthService {

	@Autowired
	public UserDetailsRepo userRepo;

	private JwtTokenCreator<UserDetailsDomain> jwtTokenCreator;

	
	@Autowired
	public AuthImpl(final JwtTokenCreator<UserDetailsDomain> jwtTokenCreator) {
		this.jwtTokenCreator = jwtTokenCreator;
	}
	
	
	@Override
	public UserToken validateUser(LoginDto login) {
		Optional<UserDetailsDomain> user = userRepo.findById(login.getUsername());

		if (user.get() == null || !(login.getPassword().equals(user.get().getPassword()))) {
			throw new JwtAuthenticationException("Invalid user name or Password");
		} else {

		}
		String token = jwtTokenCreator.createSignedJwtToken(user.get());

		UserToken us = new UserToken();

		us.setToken(token);

		us.setUsername(login.getUsername());

		return us;

	}

}
