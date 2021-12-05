package com.example.demo.auth;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.UserToken;

public interface AuthService {

	public UserToken validateUser(LoginDto login);
}
