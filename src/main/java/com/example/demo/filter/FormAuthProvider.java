package com.example.demo.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.example.demo.jwtutil.JwtTokenParser;
import com.example.demo.repo.UserDetailsRepo;

@Component
public class FormAuthProvider implements AuthenticationProvider {

	private final JwtTokenParser tokenParser;

	@Autowired
	public FormAuthProvider(JwtTokenParser tokenParser) {
		this.tokenParser = tokenParser;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		FormAuthentication auth = (FormAuthentication) authentication;

		String token = auth.getJwtToken();

		 User user = tokenParser.parseToken(token);
		return new FormAuthenticationExtraToken(user, token);
	}

	@Override
	public boolean supports(Class<?> authentication) {

		return authentication.equals(FormAuthentication.class);
	}

}
