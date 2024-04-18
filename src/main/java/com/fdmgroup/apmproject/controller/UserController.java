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

	@GetMapping("/admin/accounts")
	public String accountPage(HttpSession session, Model model) {
		User returnedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user", returnedUser);
		return "adminaccount";
	}

	@GetMapping("/admin/dashboard")
	public String adminDashboardPage(HttpSession session, Model model) {
		User returnedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user", returnedUser);
		logger.info("Redirecting to dashboard");
		return "admindashboard";
	}

	@GetMapping("/admin/profile")
	public String adminProfilePage(HttpSession session, Model model) {
		User returnedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user", returnedUser);
		return "adminprofile";
	}

	@GetMapping("/dashboard")
	public String dashboardPage(HttpSession session, Model model) {
		User returnedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user", returnedUser);
		logger.info("Redirecting to dashboard");
		return "dashboard";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.getUser().userId")
	@GetMapping("/users/{id}")
	public String profilePage(@PathVariable("id") long userId, Model model) {
		User user = userService.findUserById(userId);
		model.addAttribute("user", user);
		return "profile";
	}

	@GetMapping("/users/{id}/details")
	public String editProfilePage(@PathVariable("id") long userId, Model model) {
		logger.info("Editing Customer's profile page");
		User user = userService.findUserById(userId);
		model.addAttribute("user", user);
		return "details";
	}

	@PostMapping("/register")
	public String processRegistration(@RequestParam("username") String username,
			@RequestParam("password") String password, HttpSession session, Model model) {
		boolean isAlphanumeric = false;
		boolean hasNumbers = false;
		boolean hasLowercase = false;
		boolean hasUppercase = false;
		for (int i = 0; i < password.length(); i++) {
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
		} else if (isAlphanumeric == false) {
			model.addAttribute("errorAlphanumeric", true);
			logger.warn("Password must be alphanumeric");
			return "register";
		} else if (password.length() < 8) {
			model.addAttribute("errorLength", true);
			logger.warn("Password must be 8 characters long");
			return "register";
		} else {
			userService.persist(user);
			return "redirect:/login";
		}
	}

	@PostMapping("/users/{id}/details")
	public String editCustomerProfile(@PathVariable("id") long userId,
			@RequestParam(name = "address", required = false) String address,
			@RequestParam(name = "firstName", required = false) String firstName,
			@RequestParam(name = "lastName", required = false) String lastName, HttpSession session, Model model) {
		User tempUser = userService.findUserById(userId);
		if (!address.isEmpty()) {
			tempUser.setAddress(address);
		}
		if (!address.isEmpty()) {
			tempUser.setFirstName(firstName);
		}
		if (!address.isEmpty()) {
			tempUser.setLastName(lastName);
		}
		session.setAttribute("loggedUser", tempUser);
		model.addAttribute("user", tempUser);
		userService.update(tempUser);
		return "profile";
	}
}
