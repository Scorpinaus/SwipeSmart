package com.fdmgroup.apmproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.service.AccountService;
import com.fdmgroup.apmproject.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AccountController {
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/deposit")
	public String goToDepositPage(Model model, HttpSession session) {
		
		
		
		User currentUser = (User) session.getAttribute("loggedUser");
		
		long userId = currentUser.getUserId();
		
		accountService.findById(userId);
		
		model.addAttribute("user", currentUser);
		
		model.addAttribute("accounts", );
		
		return ("deposit");
	}
	
	
	@PostMapping("/deposit")
	public String deposit(HttpServletRequest request ) {
		

		
		String depositAmountStr = request.getParameter("deposit amount");
		Double depositAmount = Double.parseDouble(depositAmountStr);
		
		return "redirect:/account";
	}
}
