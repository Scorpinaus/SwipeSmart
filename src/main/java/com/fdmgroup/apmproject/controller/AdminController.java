package com.fdmgroup.apmproject.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.fdmgroup.apmproject.repository.AccountRepository;
import com.fdmgroup.apmproject.repository.CreditCardRepository;
import com.fdmgroup.apmproject.repository.UserRepository;
import com.fdmgroup.apmproject.service.AccountService;
import com.fdmgroup.apmproject.service.CreditCardService;
import com.fdmgroup.apmproject.service.StatusService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private	UserRepository userRepository;
	
	@Autowired
	private StatusService statusService;
	
	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private CreditCardRepository creditCardRepository;
	
	private static final Logger LOGGER = LogManager.getLogger(AccountController.class);
	
	@GetMapping("/admin/accounts")
	public String accountPage(@RequestParam("userId") long userId, HttpSession session, Model model) {
		//add  returned user to the model
		User returnedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user", returnedUser);
		
		//find all account by user Id
		List<Account> requiredAccounts = accountRepository.findByAccountUserUserId(userId);
		model.addAttribute("requiredAccounts", requiredAccounts);
		
		
		
		
		
		
		
		return "adminaccount";
	}
	

	@GetMapping("/admin/creditcards")
	public String creditcardPage(@RequestParam("userId")long userId,HttpSession session, Model model) {
		//add  returned user to the model
		User returnedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user", returnedUser);
		
		
		//find all credit card by user Id
		List<CreditCard> requiredCreditCards = creditCardRepository.findByCreditCardUserUserId(userId);
		
		model.addAttribute("requiredCreditCards", requiredCreditCards);
		
		
//		List<CreditCard> ccList = creditCardService.findAllCreditCards();
//		
//		List<Transaction> transactionList = new ArrayList<Transaction>();
//		for (CreditCard cc : ccList) {
//			List<Transaction> transaction = cc.getTransactions();
//			transactionList.addAll(transaction);
//		}
//		
//		model.addAttribute("transactions", transactionList);
//		
//		model.addAttribute("user", returnedUser);
//		
//		model.addAttribute("creditCards", ccList);
		
		return "admincreditcard";
	}

	@GetMapping("/admin/dashboard")
	public String adminDashboardPage(HttpSession session, Model model) {
		User returnedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user", returnedUser);
		LOGGER.info("Redirecting to dashboard");
		return "admindashboard";
	}

	@GetMapping("/admin/profile")
	public String adminProfilePage(HttpSession session, Model model) {
		User returnedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user", returnedUser);
		return "adminprofile";
	}
	
	@GetMapping("/admin/users")
	public String adminUserPage(HttpSession session, Model model) {
		User returnedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user", returnedUser);
		
		
		List<User> users= userRepository.findAll();
		model.addAttribute("users", users);
		
		return "adminuser";
	}
	
	
	
	
	@PostMapping("/admin/bankaccountApproval")
	public String approveBankAccount (@RequestParam("accountNumber") String accountNumber) {
		System.out.println("approve");
		Account account = accountService.findAccountByAccountNumber(accountNumber);
		
		account.setAccountStatus(statusService.findByStatusName("Approved"));
		accountService.update(account);
		long userId = account.getAccountUser().getUserId();
		return "redirect:/admin/accounts?userId=" + userId;
	}
	
	@PostMapping("/admin/credicardApproval")
	public String approveCredicard (@RequestParam("creditCardNumber") String creditCardNumber) {
		System.out.println("approve");
		CreditCard creditCard = creditCardService.findByCreditCardNumber(creditCardNumber);
		
		creditCard.setCreditCardStatus(statusService.findByStatusName("Approved"));
		creditCardService.update(creditCard);
		long userId = creditCard.getCreditCardUser().getUserId();
		return "redirect:/admin/accounts?userId=" + userId;
	}
	
	
}
