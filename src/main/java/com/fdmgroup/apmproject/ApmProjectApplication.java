package com.fdmgroup.apmproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fdmgroup.apmproject.repository.AccountRepository;
import com.fdmgroup.apmproject.service.AccountService;
import com.fdmgroup.apmproject.service.UserService;

@SpringBootApplication
public class ApmProjectApplication {
	
	
	UserService userservice;
	
	AccountService accountservice;
	
	public static void main(String[] args) {
		SpringApplication.run(ApmProjectApplication.class, args);
	}
}
