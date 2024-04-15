package com.fdmgroup.apmproject.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	private static Logger logger = LogManager.getLogger(UserController.class);

	@GetMapping("/index")
	public String welcomePage() {
		return "index";
	}

	@GetMapping("/register")
	public String registerPage() {
		return "register";
	}

	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}
	
	@GetMapping("/logout")
	public String logoutPage(HttpSession session) {
		logger.info("Customer has logged out.");
		session.invalidate();
		return "redirect:/login";
	}

	@GetMapping("/login_error")
	public String loginPageError(Model model) {
		model.addAttribute("errorMessage", true);
		logger.warn("Invalid username or password to register");
		return "login";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/account")
	public String accountPage() {
		return "account";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/creditcard")
	public String creditcardPage() {
		return "creditcard";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	@GetMapping("/dashboard")
	public String dashboardPage(HttpSession session, Model model) {
		User returnedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user", returnedUser);
		logger.info("Redirecting to dashboard");
		return "dashboard";
	}
	
	@GetMapping("/users/{id}")
	public String profilePage(@PathVariable("id") long userId, Model model) {
		User user = userService.findUserById(userId);
		model.addAttribute("user",user);
		return "profile";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
		session.setAttribute("loggedUser", userService.findUserByUsername(username));
		logger.info("User has logged in");
		return "redirect:/dashboard";
	}

	@PostMapping("/register")
	public String processRegistration(@RequestParam("username") String username,
			@RequestParam("password") String password, HttpSession session, Model model) {
		boolean isAlphanumeric = false;
		boolean hasNumbers = false;
		boolean hasLowercase = false;
		boolean hasUppercase = false;
		for (int i = 0; i < password.length() ; i++) {
			if (password.codePointAt(i) >= 48 && password.codePointAt(i) <= 57) {
				hasNumbers = true;
			}
			if (password.codePointAt(i) >= 65 && password.codePointAt(i) <= 90) {
				hasUppercase = true;
			}
			if (password.codePointAt(i) >= 97 && password.codePointAt(i) <= 122) {
				hasLowercase = true;
			}
		}
		if (hasNumbers == true && hasLowercase == true && hasUppercase == true) {
			isAlphanumeric = true;
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		User user = new User(username, encoder.encode(password), null, null, null);
		if (userService.findUserByUsername(username) != null || username.equals("") || password.equals("")) {
			model.addAttribute("errorInvalid", true);
			logger.warn("Invalid username or password to register");
			return "register";
		} 
		else if(isAlphanumeric == false) {
			model.addAttribute("errorAlphanumeric", true);
			logger.warn("Password must be alphanumeric");
			return "register";
		}
		else {
			userService.persist(user);
			return "redirect:/login";
		}
	}
}
