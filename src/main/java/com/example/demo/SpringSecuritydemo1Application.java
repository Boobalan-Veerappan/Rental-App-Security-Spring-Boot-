package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.service.NameGenerater;

@SpringBootApplication
public class SpringSecuritydemo1Application implements CommandLineRunner  {


	
	public static void main(String[] args) {
		SpringApplication.run(SpringSecuritydemo1Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		System.err.println(" BOOOOOOO ............. Boooooo ........... Boobalan ..........");
		
	
	}



}
