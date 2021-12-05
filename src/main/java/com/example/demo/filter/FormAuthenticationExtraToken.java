package com.example.demo.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class FormAuthenticationExtraToken extends UsernamePasswordAuthenticationToken{

	public FormAuthenticationExtraToken(Object principal, Object credentials) {
		super(principal, credentials);
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
