package com.example.demo.filter;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class FormAuthentication extends AbstractAuthenticationToken {

	public String userName;
	public String password;

	private final String jwtToken;

	public FormAuthentication(String jwtToken) {
		super(Collections.<GrantedAuthority>emptyList());
		this.jwtToken = jwtToken;

	}


	private static final long serialVersionUID = 1L;

	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return jwtToken;
	}

	@Override
	public Object getPrincipal() {
		return jwtToken;
	}

	public String getJwtToken() {
		return this.jwtToken;
	}

}
