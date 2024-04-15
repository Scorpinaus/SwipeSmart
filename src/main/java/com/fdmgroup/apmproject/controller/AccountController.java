package com.fdmgroup.apmproject.controller;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.service.AccountService;
import com.fdmgroup.apmproject.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AccountController {
	
	@Autowired
	private AccountService accountService;
		
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
				model.addAttribute("currentUserBankAccounts", userBankAccounts);
			}
			return "accountdashboard";
			
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
	
	@GetMapping("/bankaccount/deposit")
	public String goToDepositPage(Model model, HttpSession session) {
		
		//Get logged user
		User currentUser = (User) session.getAttribute("loggedUser");
		
		//Get user id
		long userId = currentUser.getUserId();
		
		//get all the accounts owned by that user
		List<Account> AccountList = accountService.findAllAccountsByUserId(userId);
		
		//add user and account list to the model
		model.addAttribute("user", currentUser);
		model.addAttribute("AccountList", AccountList);
		
		return ("deposit");
	}
	
	
	@PostMapping("/bankaccount/deposit")
	public String deposit(	@RequestParam("account") long accountId,
            				@RequestParam("deposit amount") double depositAmount
            				) {
		
		// get the required account
		Account accountDeposited = accountService.findById(accountId);
		
		//Calculate the balance after deposit
		Double updatedBalance = accountDeposited.getBalance() + depositAmount;
		
		//update the account balance
		accountDeposited.setBalance(updatedBalance);
		accountService.update(accountDeposited);

		return "redirect:/bankaccount/dashboard";
	}
	
	@GetMapping("/bankaccount/create")
	public String goToCreateBankAccountPage(Model model, HttpSession session) {
	
		return "createbankaccount";
	}
	
	@PostMapping("/bankaccount/create")
	public String createBankAccount(	
		@RequestParam("accountName") String accountName,
		@RequestParam("initialDeposit") double initialDeposit,
		HttpSession session)
	{
		//Get logged user
		User currentUser = (User) session.getAttribute("loggedUser");
		
		
		String accountnumber = accountService.generateUniqueAccountNumber();
		
		Account accountCreated = new Account(accountName,initialDeposit,accountnumber,currentUser);
		
		accountService.persist(accountCreated);
		
		return "redirect:/bankaccount/dashboard";
	}
	
	
}
