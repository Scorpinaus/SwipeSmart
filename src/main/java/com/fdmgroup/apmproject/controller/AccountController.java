package com.fdmgroup.apmproject.controller;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdmgroup.apmproject.model.Account;
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
	
	private static Logger logger = LogManager.getLogger(AccountController.class);
	
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	//Function brings logged on user to BankAccounts page.
	@GetMapping("/bankaccount/dashboard")
	public String showBankAccountDashboard(HttpSession session, Model model) {
		if (session != null && session.getAttribute("loggedUser") !=null) {
			//retrieves current user from current session
			User currentUser = (User) session.getAttribute("loggedUser");
			//retrieves all active bank accounts under current user
			List<Account> userBankAccounts = currentUser.getAccounts();
			if (userBankAccounts.size() != 0) {
				model.addAttribute("currentUserBankAccounts", userBankAccounts);
				logger.info("User is redirected to bank account dashboard");	
			} else {
				logger.info("User is redirected to bank account. User has no active bank accounts with the bank");
			}
			return "accountdashboard";
			
		} else {
			return "redirect:/login";
		}
	}
	
	//Function which brings user to selected BankAccounts page & view list of transactions
	@GetMapping("/bankaccount/{id}")
	public String showBankAccountTransactions(HttpSession session, Model model) {
		if (session !=null && session.getAttribute("loggedUser") !=null) {
			//retrieves user from current session
			User currentUser = (User) session.getAttribute("loggedUser");		
			return "/bankaccount/{id}";
		} else {
			return "redirect:/login";
		}
		
	}
	
	//Function which brings user to Bank Account withdrawal page
	@GetMapping("/bankaccount/withdrawal")
	public String withdrawalBankAccount(HttpSession session, Model model) {
		if (session !=null && session.getAttribute("loggedUser")!=null){
			User currentUser = (User) session.getAttribute("loggedUser");
			model.addAttribute("user", currentUser);
			List<Account> accounts = currentUser.getAccounts();
			if (accounts.isEmpty()) {
				model.addAttribute("error", "No bank accounts found");
				return "/bankaccount/withdrawal";
			}
			model.addAttribute("accounts", accounts);
			return "/bankaccount/withdrawal";
		}
		else {
			return "redirect:/login";
		}
		
	}
	
	@PostMapping("/processWithdrawal")
	public String processWithdrawal(@RequestParam Long accountId,@RequestParam BigDecimal amount, HttpSession session, RedirectAttributes redirectAttributes) {
		if (session != null && session.getAttribute("loggedUser") !=null) {
			//Checking for account ownership & if funds in bank account is sufficient. Returns respective message if successful or failure.
			boolean success = accountService.withdrawAccountByAmount(accountId, amount);
			if (success) {
				redirectAttributes.addFlashAttribute("message", "Withdrawal successful!");
				return "redirect:/bankaccount/dashboard";
			} else {
				redirectAttributes.addFlashAttribute("error", "Withdrawal failed!");
				return "redirect:/backaccount/withdrawal";
			}
		} else {
			return "redirect:/login";
		}
		
	}
	
	@GetMapping("/deposit")
	public String goToDepositPage(Model model, HttpSession session) {
		

		User currentUser = (User) session.getAttribute("loggedUser");
		
		long userId = currentUser.getUserId();
		
		List<Account> AccountList = accountService.findAllAccountsByUserId(userId);
		AccountList.forEach(System.out::println);
		System.out.println(accountService.findAllAccountsByUserId(userId));
		model.addAttribute("user", currentUser);
		
		model.addAttribute("AccountList", AccountList);
		
		return ("deposit");
	}
	
	
	@PostMapping("/deposit")
	public String deposit(HttpServletRequest request ) {
		

		String depositAmountStr = request.getParameter("deposit amount");
		
		Double depositAmount = Double.parseDouble(depositAmountStr);
		
		
//		Account accountDeposited = accountService
		
		return "redirect:/account";
	}
}
