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
	TransactionService transactionService;
	
	private static Logger logger = LogManager.getLogger(CreditCardController.class);
	
	
	@GetMapping("/viewTransactions")
	public String viewCardTransactions(@RequestParam(name = "creditCardId", required = false) String creditCardId, @RequestParam(name = "accountId", required = false) String accountId, Model model,
			HttpSession session) {
		if (!(session != null && session.getAttribute("loggedUser") != null)) {
			model.addAttribute("error", true);
			logger.warn("User Is not logged-in. Please login first");
			return "userCards";
		} else {
			
			User loggedUser = (User) session.getAttribute("loggedUser");
			model.addAttribute("user", loggedUser);
			if (accountId != null) {
				Account userAccount = accountService.findById(Long.parseLong(accountId));
				if (session.getAttribute("transactionFilter") == null) {
					List<Transaction> transactions = userAccount.getTransactions();
					model.addAttribute("transactions", transactions);
				}else {
					
					System.out.println("filtered");
					Map<String, Object> transactionFilterMap = (Map<String, Object>) session.getAttribute("transactionFilter");
//					
					String transactionType = (String) transactionFilterMap.get("transactionType");
//					
					
//					String transactionType = (String) session.getAttribute("transactionType");
					
					List<Transaction> transactions = transactionService.getTransactionsByDateAmountAndType(30
							, transactionType, 0);
					
					model.addAttribute("transactions", transactions);
//					List<Transaction> transactions = transactionService.getTransactionsByDateAmountAndType(30
//							, "deposit", 0);
				}
				
				
				model.addAttribute("account", userAccount);
				
			} else if (creditCardId != null) {
				CreditCard userCreditCard = creditCardService.findById(Long.parseLong(creditCardId));
				List<Transaction> transactions = userCreditCard.getTransactions();
				model.addAttribute("creditCard", userCreditCard);
				model.addAttribute("transactions", transactions);
				System.out.println(transactions);
			}
			return "viewTransactions";
			
			
		}
	}
	
	
	@PostMapping("transaction/transactionFilter")
	public String transactionFilter (@RequestParam("transactionType") String transactionType, HttpSession session) {
		Map<String, Object> transactionFilterMap = new HashMap<>();
		transactionFilterMap.put("transactionType", transactionType);
		session.setAttribute("transactionFilter", transactionFilterMap);
		
		
		return "redirect:/viewTransactions";
	}
	
}
