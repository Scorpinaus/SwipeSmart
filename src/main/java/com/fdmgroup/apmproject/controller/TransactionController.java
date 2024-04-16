package com.fdmgroup.apmproject.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.service.AccountService;
import com.fdmgroup.apmproject.service.CreditCardService;

import jakarta.servlet.http.HttpSession;

@Controller
public class TransactionController {
	
	@Autowired
	private CreditCardService creditCardService;
	@Autowired
	private AccountService accountService;
	
	private static Logger logger = LogManager.getLogger(CreditCardController.class);
	
	
	@GetMapping("/viewTransactions")
	public String viewCardTransactions(@RequestParam(name = "creditCardId", required = false) String creditCardId, @RequestParam(name = "accountId", required = false) String accountId, Model model,
			HttpSession session) {
		if (session != null && session.getAttribute("loggedUser") != null) {
			User loggedUser = (User) session.getAttribute("loggedUser");
			model.addAttribute("user", loggedUser);
			if (accountId != null) {
				Account userAccount = accountService.findById(Long.parseLong(accountId));
				List<Transaction> transactions = userAccount.getTransactions();
				model.addAttribute("account", userAccount);
				model.addAttribute("transactions", transactions);
			} else if (creditCardId != null) {
				CreditCard userCreditCard = creditCardService.findById(Long.parseLong(creditCardId));
				List<Transaction> transactions = userCreditCard.getTransactions();
				model.addAttribute("creditCard", userCreditCard);
				model.addAttribute("transactions", transactions);
				System.out.println(transactions);
			}
			return "viewTransactions";
		} else {
			model.addAttribute("error", true);
			logger.warn("User Is not logged-in. Please login first");
			return "userCards";
		}
	}
}
