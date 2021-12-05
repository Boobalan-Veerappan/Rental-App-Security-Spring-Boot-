package com.example.demo.controller;

import java.beans.Beans;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.UserDetailsDomain;
import com.example.demo.dto.LoginDto;
import com.example.demo.dto.UserDetailsDto;
import com.example.demo.dto.UserToken;
import com.example.demo.repo.UserDetailsRepo;
import com.example.demo.*;
import com.example.demo.auth.AuthService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class ServiceController {
		
	@Autowired
	public UserDetailsManager manager;
	
	@Autowired
	public UserDetailsRepo repo;
	
	@Autowired
	public AuthService auth;
	
	@GetMapping("/hello")
	@ResponseBody
	public String Returnhello() {
		return "boobalan";
	}
	
	@GetMapping("/dashboard")
	public String dashboard() {
		return "/WEB-INF/view/dashboard.jsp";
	}
	@GetMapping("/")
	public void se(Principal pr)
	{
		System.err.println(" pr "+pr);
	}
	
	
	@PostMapping("/login")
	public UserToken loginForm(@RequestBody LoginDto login,HttpServletRequest request)
	{
	
		return auth.validateUser(login);
			
	}
	
	@PostMapping("/registration")
	public ResponseEntity<String> registrationForm(@RequestBody UserDetailsDto userdet)
	{
		System.err.println(" userdet "+userdet.toString());
		UserDetailsDomain domain = new UserDetailsDomain();
		
		BeanUtils.copyProperties(userdet, domain);
		domain.setImage_url(userdet.getImage_url());
		repo.save(domain);
		
		return ResponseEntity.ok().body("sucess");
	}
}
