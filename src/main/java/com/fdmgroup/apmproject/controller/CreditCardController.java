package com.fdmgroup.apmproject.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.Status;
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.service.AccountService;
import com.fdmgroup.apmproject.service.CreditCardService;
import com.fdmgroup.apmproject.service.StatusService;
import jakarta.servlet.http.HttpSession;

@Controller
public class CreditCardController {
	
	@Autowired
	private CreditCardService creditCardService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private StatusService statusService;
	private static Logger logger = LogManager.getLogger(CreditCardController.class);
	
	
	@GetMapping("/userCards") 
	public String viewCreditCards(Model model, HttpSession session) {
		if (session != null && session.getAttribute("loggedUser") != null) {
			User loggedUser = (User) session.getAttribute("loggedUser");
			List<CreditCard> userCreditCards = loggedUser.getCreditCards();
			model.addAttribute("cards", userCreditCards);
			return "userCards";
		} else {
			model.addAttribute("error", true);
			logger.warn("User Is not logged-in. Please login first");
			return "userCards";
		}
	}
	
	@GetMapping("/viewTransactions") 
	public String viewCardTransactions(@RequestParam(name = "number", required = false) String number, Model model, HttpSession session) {
		if (session != null && session.getAttribute("loggedUser") != null) {
			if (number.length() == 11) {
				Account userAccount = accountService.findAccountByAccountNumber(number);
				List<Transaction> transactions = userAccount.getTransactions();
				model.addAttribute("account", userAccount);
				model.addAttribute("transactions", transactions);
			} else if (number.length() == 19) {
				CreditCard userCreditCard = creditCardService.findByCreditCardNumber(number);
				List<Transaction> transactions = userCreditCard.getTransactions();
				model.addAttribute("creditCard", userCreditCard);
				model.addAttribute("transactions", transactions);
			}
			return "/viewTransactions";
		} else {
			model.addAttribute("error", true);
			logger.warn("User Is not logged-in. Please login first");
			return "userCards";
		}
	}
	
	@GetMapping("/applyCreditCard") 
	public String applyCreditCard(Model model, HttpSession session) {
		if (session != null && session.getAttribute("loggedUser") != null) {
			return "applyCreditCard";
		} else {
			return "applyCreditCard";
		}
	}
	
	@PostMapping("/applyCreditCard")
	public String applyCreditCard(Model model, HttpSession session, @RequestParam String monthlySalary, @RequestParam String cardType) {
		if (monthlySalary.isBlank() || cardType.isBlank()) {
			logger.warn("There are empty fields, please fill up");
			model.addAttribute("error", true);
			return "applyCreditCard";
		} else {
			if (Double.parseDouble(monthlySalary) > 1000) {
				logger.warn("Your salary is too low. You are required to have a monthly salary above $1000");
				model.addAttribute("error2", true);
				return "applyCreditCard";
			} else {
				User loggedUser = (User) session.getAttribute("loggedUser");
				String creditCardNumber = creditCardService.generateCreditCardNumber();
				String pin = creditCardService.generatePinNumber();
				double cardLimit = Double.parseDouble(monthlySalary) * 3;
				// Default approved
				Status statusName = statusService.findByStatusName("Approved");
				CreditCard createCreditCard = new CreditCard(creditCardNumber, pin, cardLimit, cardType, statusName, 0, loggedUser);
				creditCardService.persist(createCreditCard);
				logger.info("Credit card of number " + creditCardNumber + " created");
				return "userCards";
			}
		}
	}
	
	
}
