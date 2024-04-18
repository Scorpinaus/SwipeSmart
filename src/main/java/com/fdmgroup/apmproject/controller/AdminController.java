package com.fdmgroup.apmproject.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.fdmgroup.apmproject.service.AccountService;
import com.fdmgroup.apmproject.service.CreditCardService;
import com.fdmgroup.apmproject.service.StatusService;
import com.fdmgroup.apmproject.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	private AccountService accountService;
	@Autowired
	private UserService userService;

	@Autowired
	private StatusService statusService;

	@Autowired
	private CreditCardService creditCardService;

	private static final Logger LOGGER = LogManager.getLogger(AccountController.class);

	@GetMapping("/admin/accounts")
	public String accountPage(@RequestParam("userId") long userId, HttpSession session, Model model) {
		User returnedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user", returnedUser);

		List<Account> requiredAccounts = accountRepository.findByAccountUserUserId(userId);
		model.addAttribute("requiredAccounts", requiredAccounts);
		return "adminaccount";
	}

	@GetMapping("/admin/creditcards")
	public String creditcardPage(HttpSession session, Model model) {
		User returnedUser = (User) session.getAttribute("loggedUser");
		List<CreditCard> ccList = creditCardService.findAllCreditCards();
		List<Transaction> transactionList = new ArrayList<Transaction>();
		for (CreditCard cc : ccList) {
			List<Transaction> transaction = cc.getTransactions();
			transactionList.addAll(transaction);
		}
		model.addAttribute("transactions", transactionList);
		model.addAttribute("user", returnedUser);
		model.addAttribute("creditCards", ccList);
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

		List<User> users = userService.findAllUsers();
		model.addAttribute("users", users);

		return "adminuser";
	}

	@PostMapping("/admin/bankaccountApproval")
	public String approveBankAccount(@RequestParam("accountNumber") String accountNumber) {
		System.out.println("approve");
		Account account = accountService.findAccountByAccountNumber(accountNumber);

		account.setAccountStatus(statusService.findByStatusName("Approved"));
		accountService.update(account);
		long userId = account.getAccountUser().getUserId();
		return "redirect:/admin/accounts?userId=" + userId;
	}

	@GetMapping("/admin/transactions")
	public String transactionPage(HttpSession session, Model model) {
		User returnedUser = (User) session.getAttribute("loggedUser");
		List<User> userList = userService.findAllUsers();
		List<CreditCard> ccList = creditCardService.findAllCreditCards();
		List<Account> accountList = accountService.getAllAccounts();
		List<Transaction> transactionList = new ArrayList<Transaction>();
		for (CreditCard cc : ccList) {
			List<Transaction> transaction = cc.getTransactions();
			transactionList.addAll(transaction);
		}
		for (Account a : accountList) {
			List<Transaction> transaction = a.getTransactions();
			transactionList.addAll(transaction);
		}
		model.addAttribute("transactions", transactionList);
		model.addAttribute("user", returnedUser);
		model.addAttribute("users", userList);
		return "admintransactions";
	}

	@PostMapping("/admin/transactions")
	public String adminViewAllTransactions(@RequestParam(name = "month", required = false) String month,
			@RequestParam(name = "pickedUser", required = false) String pickedUser, Model model, HttpSession session) {
		User loggedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user", loggedUser);
		List<User> userList = userService.findAllUsers();
		List<Transaction> transactions = new ArrayList<>();
		List<CreditCard> creditCards = creditCardService.findAllCreditCards();
		List<Account> accountList = accountService.getAllAccounts();
		if (month == null || month == "") {
			for (CreditCard cc : creditCards) {
				List<Transaction> transaction = cc.getTransactions();
				for (Transaction tx : transaction) {
					if (tx.getTransactionCreditCard().getCreditCardUser().getUsername().equals(pickedUser)) {
						transactions.add(tx);
					}
				}
			}
			for (Account a : accountList) {
				List<Transaction> transaction = a.getTransactions();
				for (Transaction tx : transaction) {
					if (tx.getTransactionAccount().getAccountUser().getUsername().equals(pickedUser)) {
						transactions.add(tx);
					}
				}
			}
			Collections.sort(transactions, Comparator.comparing(Transaction::getTransactionDate));
		} else {
			int year = Integer.parseInt(month.substring(0, 4));
			int monthValue = Integer.parseInt(month.substring(5, 7));
			for (CreditCard cc : creditCards) {
				List<Transaction> transaction = cc.getTransactions();
				for (Transaction t : transaction) {
					if ((t.getTransactionDate().getMonthValue() == monthValue
							&& t.getTransactionDate().getYear() == year)
							&& (t.getTransactionCreditCard().getCreditCardUser().getUsername().equals(pickedUser))) {
						transactions.add(t);
					}
				}
			}
			for (Account a : accountList) {
				List<Transaction> transaction = a.getTransactions();
				for (Transaction t : transaction) {
					if ((t.getTransactionDate().getMonthValue() == monthValue
							&& t.getTransactionDate().getYear() == year)
							&& (t.getTransactionAccount().getAccountUser().getUsername().equals(pickedUser))) {
						transactions.add(t);
					}
				}
			}
			Collections.sort(transactions, Comparator.comparing(Transaction::getTransactionDate));
		}
		model.addAttribute("users", userList);
		model.addAttribute("transactions", transactions);
		return "admintransactions";
	}

}
