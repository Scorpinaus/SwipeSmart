package com.fdmgroup.apmproject.controller;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.service.AccountService;
import com.fdmgroup.apmproject.service.CreditCardService;
import com.fdmgroup.apmproject.service.TransactionService;

import jakarta.servlet.http.HttpSession;

@Controller
public class TransactionController {
	
	@Autowired
	private CreditCardService creditCardService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private TransactionService transactionService;
	
	 	
	private static Logger logger = LogManager.getLogger(CreditCardController.class);
	
	
	@GetMapping("/viewTransactions")
	public String viewCardTransactions(@RequestParam(name = "transactionType", required = false) String transactionType, @RequestParam(name = "month", required = false) String month, @RequestParam(name = "creditCardId", required = false) String creditCardId, @RequestParam(name = "accountId", required = false) String accountId, Model model,
			HttpSession session) {
		
		if (!(session != null && session.getAttribute("loggedUser") != null)) {
			model.addAttribute("error", true);
			logger.warn("User Is not logged-in. Please login first");
			return "userCards";
		} else {
			
			User loggedUser = (User) session.getAttribute("loggedUser");
			model.addAttribute("user", loggedUser);
			List<Transaction> transactions = new ArrayList<>();
			if (accountId != null) {
				Account userAccount = accountService.findById(Long.parseLong(accountId));

				if (month == null || month == "") {
					transactions = userAccount.getTransactions();
				} else {
					int year = Integer.parseInt(month.substring(0, 4));
				    int monthValue = Integer.parseInt(month.substring(5));
				    transactions = transactionService.getTransactionsByMonthAndYearAndTransactionAccount(year, monthValue, userAccount);
				}

				model.addAttribute("transactions", transactions);
				model.addAttribute("account", userAccount);
				
			} else if (creditCardId != null) {
				CreditCard userCreditCard = creditCardService.findById(Long.parseLong(creditCardId));
				if (month == null || month == "") {
					transactions = userCreditCard.getTransactions();
				} else {
					int year = Integer.parseInt(month.substring(0, 4));
					System.out.println(year);
				    int monthValue = Integer.parseInt(month.substring(5));
				    System.out.println(monthValue);
				    transactions = transactionService.getTransactionsByMonthAndYearAndTransactionCreditCard(year, monthValue, userCreditCard);
				    System.out.println(transactions);
				}
				model.addAttribute("creditCard", userCreditCard);
				model.addAttribute("transactions", transactions);
			}
			return "viewTransactions";
			
			
		}
	}
}
