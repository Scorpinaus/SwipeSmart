package com.fdmgroup.apmproject.controller;

import java.util.List;

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

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.service.AccountService;
import com.fdmgroup.apmproject.service.CreditCardService;
import com.fdmgroup.apmproject.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * This class is a Spring MVC controller responsible for handling requests related to users.
 * It provides methods for registration, login, logout, viewing profiles, and editing profiles.
 *
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */
@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired 
	private CreditCardService creditCardService;
	
	@Autowired
	private AccountService accountService;

	private static Logger logger = LogManager.getLogger(UserController.class);
	/**
	 * This class is a Spring MVC controller responsible for handling requests related to users.
	 * It provides methods for registration, login, logout, viewing profiles, and editing profiles.
	 *
	 * @author Bard
	 * @version 1.0
	 * @since 2024-04-22
	 */
	@GetMapping("/index")
	public String welcomePage() {
		return "index";
	}
	/**
     * This method displays the registration page.
     *
     * @return The name of the view to be rendered.
     */
	@GetMapping("/register")
	public String registerPage() {
		return "register";
	}

    /**
     * This method displays the login page.
     *
     * @return The name of the view to be rendered.
     */
	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}
    /**
     * This method logs out the user and redirects to the login page.
     *
     * @param session The HTTP session containing the logged-in user information.
     * @return The name of the view to be rendered.
     */
	@GetMapping("/logout")
	public String logoutPage(HttpSession session) {
		logger.info("Customer has logged out.");
		session.invalidate();
		return "redirect:/login";
	}
    /**
     * This method displays the login page with an error message.
     *
     * @param model The model to be used for rendering the view.
     * @return The name of the view to be rendered.
     */
	@GetMapping("/login_error")
	public String loginPageError(Model model) {
		model.addAttribute("errorMessage", true);
		logger.warn("Invalid username or password to register");
		return "login";
	}
    /**
     * This method displays the dashboard page for the logged-in user.
     *
     * @param session The HTTP session containing the logged-in user information.
     * @param model The model to be used for rendering the view.
     * @return The name of the view to be rendered.
     */
	@GetMapping("/dashboard")
	public String dashboardPage(HttpSession session, Model model) {
		User returnedUser = (User) session.getAttribute("loggedUser");
		List<CreditCard> userCreditCards = creditCardService.findAllCreditCardByUserId(returnedUser.getUserId());
		List<Account> userBankAccounts = accountService.findAllAccountsByUserId(returnedUser.getUserId());
		model.addAttribute("user", returnedUser);
		model.addAttribute("cards", userCreditCards);
		model.addAttribute("currentUserBankAccounts", userBankAccounts);
		logger.info("Redirecting to dashboard");
		return "dashboard";
	}
    /**
     * This method displays the profile page for the specified user.
     *
     * @param userId The ID of the user to display the profile for.
     * @param model The model to be used for rendering the view.
     * @return The name of the view to be rendered.
     */
	@PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.getUser().userId")
	@GetMapping("/users/{id}")
	public String profilePage(@PathVariable("id") long userId, Model model) {
		User user = userService.findUserById(userId);
		model.addAttribute("user", user);
		return "profile";
	}
    /**
     * This method displays the edit profile page for the specified user.
     *
     * @param userId The ID of the user to display the edit profile page for.
     * @param model The model to be used for rendering the view.
     * @return The name of the view to be rendered.
     */
	@GetMapping("/users/{id}/details")
	public String editProfilePage(@PathVariable("id") long userId, Model model) {
		logger.info("Editing Customer's profile page");
		User user = userService.findUserById(userId);
		model.addAttribute("user", user);
		return "details";
	}
    /**
     * This method processes a registration request.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @param session The HTTP session containing the logged-in user information.
     * @param model The model to be used for rendering the view.
     * @return The name of the view to be rendered.
     */
	@PostMapping("/register")
	public String processRegistration(@RequestParam("username") String username,
			@RequestParam("password") String password, HttpSession session, Model model) {
		boolean isAlphanumeric = false;
		boolean hasNumbers = false;
		boolean hasLowercase = false;
		boolean hasUppercase = false;
		
		// Password validation to set boolean settings
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
		//Final check if password meets requirement of having upper and lower case and has numbers to meet requirements of a strong password
		if (hasNumbers == true && hasLowercase == true && hasUppercase == true) {
			isAlphanumeric = true;
		}
		
		//Uses Spring Security's password encoder to safely encode password
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		User user = new User(username, encoder.encode(password), null, null, null);
		
		//Error validation step
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
    /**
     * This method processes an edit profile request.
     *
     * @param userId The ID of the user to edit.
     * @param address The new address of the user.
     * @param firstName The new first name of the user.
     * @param lastName The new last name of the user.
     * @param session The HTTP session containing the logged-in user information.
     * @param model The model to be used for rendering the view.
     * @return The name of the view to be rendered.
     */
	@PostMapping("/users/{id}/details")
	public String editCustomerProfile(@PathVariable("id") long userId,
			@RequestParam(name = "address", required = false) String address,
			@RequestParam(name = "firstName", required = false) String firstName,
			@RequestParam(name = "lastName", required = false) String lastName, HttpSession session, Model model) {
		//Finds user by currently logged on user userId
		User tempUser = userService.findUserById(userId);
		
		//Checks if respective attribute field is empty. If not empty, update current user attribute field.
		if (!address.isEmpty()) {
			tempUser.setAddress(address);
		}
		if (!firstName.isEmpty()) {
			tempUser.setFirstName(firstName);
		}
		if (!lastName.isEmpty()) {
			tempUser.setLastName(lastName);
		}
		session.setAttribute("loggedUser", tempUser);
		model.addAttribute("user", tempUser);
		userService.update(tempUser);
		return "profile";
	}
}
