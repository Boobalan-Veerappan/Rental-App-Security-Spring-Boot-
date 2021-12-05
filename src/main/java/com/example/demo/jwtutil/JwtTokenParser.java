package com.example.demo.jwtutil;

import org.springframework.security.core.userdetails.User;

public interface JwtTokenParser {
	 User parseToken(String jwToken);
}
