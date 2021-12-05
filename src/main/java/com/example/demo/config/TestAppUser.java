package com.example.demo.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class TestAppUser extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private final String userId;
    private final String role;
    
    public TestAppUser(String username, String userId, String role, Collection<? extends GrantedAuthority> authorities) {
        super(username, "", authorities);
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "DsUser{" + "userId=" + userId + ", role=" + role + '}';
    }
}
