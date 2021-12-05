package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
public class SecurityBuilder {

	@Bean
	public UserDetailsManager userdetails()
	{
		return new InMemoryUserDetailsManager();
	}
}
