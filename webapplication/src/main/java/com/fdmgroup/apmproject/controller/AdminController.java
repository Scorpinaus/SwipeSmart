package com.fdmgroup.apmproject.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.fdmgroup.apmproject.service.StatusService;
import com.fdmgroup.apmproject.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * This class is a Spring MVC controller responsible for handling requests related to admin functionalities.
 * It provides methods for managing user accounts, credit cards, and transactions.
 *
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */
@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private UserService userService;

	@Autowired
	private StatusService statusService;

	@Autowired
	private CreditCardService creditCardService;

	private static final Logger LOGGER = LogManager.getLogger(AccountController.class);

	/**
	 * This method displays the user's bank accounts and allows the admin to set the
	 * status of each account.
	 *
	 * @param userId  The ID of the user whose accounts are to be displayed.
	 * @param session The HTTP session object.
	 * @param model   The model object used to pass data to the view.
	 * @return The name of the view to render.
	 */
	@GetMapping("/admin/accounts")
	public String accountPage(@RequestParam("userId") long userId, HttpSession session, Model model) {
		// Finds returned user based on input userId and adds returnedUser as a model attribute for front-end viewing
		User returnedUser = userService.findUserById(userId);
		model.addAttribute("user", returnedUser);
		// find all bank account by returnedUser and adds it as a model attribute for front-end viewing.
		List<Account> requiredAccounts = returnedUser.getAccounts();
		model.addAttribute("requiredAccounts", requiredAccounts);
		return "admin/admin-account";
	}

	/**
	 * This method displays the user's credit cards and allows the admin to set the
	 * status of each card.
	 *
	 * @param userId  The ID of the user whose credit cards are to be displayed.
	 * @param session The HTTP session object.
	 * @param model   The model object used to pass data to the view.
	 * @return The name of the view to render.
	 */
	@GetMapping("/admin/creditcards")
	public String creditcardPage(@RequestParam("userId") long userId, HttpSession session, Model model) {
		// Searches for returnedUser based on input UserId and adds user as an attribute to be used for front-end
		User returnedUser = userService.findUserById(userId);
		model.addAttribute("user", returnedUser);

		// Find and returns all credit card that the returnedUser has. Adds the list as an attribute to be used for front-end viewing
		List<CreditCard> requiredCreditCards = returnedUser.getCreditCards();
		model.addAttribute("requiredCreditCards", requiredCreditCards);

		return "admin/admin-creditcard";
	}

	/**
	 * This method displays the admin dashboard.
	 *
	 * @param session The HTTP session object.
	 * @param model   The model object used to pass data to the view.
	 * @return The name of the view to render.
	 */
	@GetMapping("/admin/dashboard")
	public String adminDashboardPage(HttpSession session, Model model) {
		// Finds and return currentUser, adds as a model attribute and returns user to admin dashboard.
		User returnedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user", returnedUser);
		LOGGER.info("Redirecting to dashboard");
		return "admin/admin-dashboard";
	}

	/**
	 * This method displays a list of all users.
	 *
	 * @param session The HTTP session object.
	 * @param model   The model object used to pass data to the view.
	 * @return The name of the view to render.
	 */
	@GetMapping("/admin/users")
	public String adminUserPage(HttpSession session, Model model) {
		// Finds and returns current Admin User and adds to model attribute for front-end processing
		User returnedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user", returnedUser);
		
		//Searches for all active users and adds to model attribute for front-end view. Redirects admin to view all users.
		List<User> users = userService.findAllUsers();
		model.addAttribute("users", users);

		return "admin/admin-user";
	}

	/**
	 * This method sets the status of a bank account.
	 *
	 * @param status        The new status of the account.
	 * @param accountNumber The account number of the account to be updated.
	 * @param session       The HTTP session object.
	 * @return The name of the view to render.
	 */
	@PostMapping("/admin/bankaccountStatus")
	public String setBankAccountStatus(@RequestParam("status") String status,
			@RequestParam("accountNumber") String accountNumber, HttpSession session) {
		
		//Retrieves selected bank account and updates selected bank account status. Updates bank account details onto database
		Account account = accountService.findAccountByAccountNumber(accountNumber);
		account.setAccountStatus(statusService.findByStatusName(status));
		accountService.update(account);

		LOGGER.info("Account Id: " + account.getAccountId() + "'s status has been setted to " + status + " by "
				+ ((User) session.getAttribute("loggedUser")).getUsername());
		
		//Redirects admin user to webpage that shows all bank account under user.
		long userId = account.getAccountUser().getUserId();
		return "redirect:/admin/accounts?userId=" + userId;
	}

	/**
	 * This method approves a credit card.
	 *
	 * @param creditCardNumber The credit card number of the card to be approved.
	 * @param session          The HTTP session object.
	 * @return The name of the view to render.
	 */
	@PostMapping("/admin/credicardApproval")
	public String approveCredicard(@RequestParam("creditCardNumber") String creditCardNumber, HttpSession session) {
		// Retrieves pending credit card based on credit card number, updates the status and updates entry onto the database.
		CreditCard creditCard = creditCardService.findByCreditCardNumber(creditCardNumber);
		creditCard.setCreditCardStatus(statusService.findByStatusName("Approved"));
		creditCardService.update(creditCard);
		
		// Logs change and brings admin user back to credit card page.
		LOGGER.info("creditcard Id: " + creditCard.getCreditCardId() + " has been approved by "
				+ ((User) session.getAttribute("loggedUser")).getUsername());
		long userId = creditCard.getCreditCardUser().getUserId();
		return "redirect:/admin/creditcards?userId=" + userId;
	}
	
	/**
	 * This method sets the status of a credit card.
	 *
	 * @param status The new status of the credit card.
	 * @param creditCardNumber The credit card number of the card to be updated.
	 * @param session The HTTP session object.
	 * @return The name of the view to render.
	 */
	@PostMapping("/admin/credicardStatus")
	public String setCredicardStatus(@RequestParam("status") String status,
			@RequestParam("creditCardNumber") String creditCardNumber, HttpSession session) {
		
		// Retrieves credit card based on credit card number, updates the status and updates entry onto the database.
		CreditCard creditCard = creditCardService.findByCreditCardNumber(creditCardNumber);

		creditCard.setCreditCardStatus(statusService.findByStatusName(status));
		creditCardService.update(creditCard);
		
		// Logs change and brings admin user back to credit card page.
		LOGGER.info("creditcard Id: " + creditCard.getCreditCardId() + "'s status has been setted to " + status + " by "
				+ ((User) session.getAttribute("loggedUser")).getUsername());
		long userId = creditCard.getCreditCardUser().getUserId();
		return "redirect:/admin/creditcards?userId=" + userId;
	}
	
	/**
	 * This method displays a list of all transactions.
	 *
	 * @param session The HTTP session object.
	 * @param model The model object used to pass data to the view.
	 * @return The name of the view to render.
	 */
	@GetMapping("/admin/transactions")
	public String transactionPage(HttpSession session, Model model) {
		//Retrieves current admin user and list of all active users, credit card, bank account and transactions.
		User returnedUser = (User) session.getAttribute("loggedUser");
		List<User> userList = userService.findAllUsers();
		List<CreditCard> ccList = creditCardService.findAllCreditCards();
		List<Account> accountList = accountService.getAllAccounts();
		List<Transaction> transactionList = new ArrayList<Transaction>();
		
		// Prepares list of transactions for all credit cards in the list
		for (CreditCard cc : ccList) {
			List<Transaction> transaction = cc.getTransactions();
			transactionList.addAll(transaction);
		}
		
		//Prepares list of transactions for all bank accounts in the list
		for (Account a : accountList) {
			List<Transaction> transaction = a.getTransactions();
			transactionList.addAll(transaction);
		}
		
		//Adds list of transactions depending if credit card or bank account is called by front-end and adds the model attributes to be shown on the front-end.
		model.addAttribute("transactions", transactionList);
		model.addAttribute("user", returnedUser);
		model.addAttribute("users", userList);
		return "admin/admin-transactions";
	}
	
	
	/**
	 * This method filters and displays transactions based on the selected month, user, and type.
	 *
	 * @param month The month for which transactions are to be displayed.
	 * @param pickedUser The username of the user whose transactions are to be displayed.
	 * @param pickedType The type of transaction to be displayed (card or account).
	 * @param model The model object used to pass data to the view.
	 * @param session The HTTP session object.
	 * @return The name of the view to render.
	 */
	@PostMapping("/admin/transactions")
	public String adminViewAllTransactions(@RequestParam(name = "month", required = false) String month,
			@RequestParam(name = "pickedUser", required = false) String pickedUser,
			@RequestParam(name = "pickedType", required = false) String pickedType, Model model, HttpSession session) {
		
		//Retrieves current admin user and list of all active users, credit card, bank account and transactions.
		User loggedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user", loggedUser);
		List<User> userList = userService.findAllUsers();
		List<Transaction> transactions = new ArrayList<>();
		List<CreditCard> creditCards = creditCardService.findAllCreditCards();
		List<Account> accountList = accountService.getAllAccounts();
		
		//Filters based on month, type (either credit card or bank account) or specific user.
		if (month == null || month == "") {
			if (pickedType == null) {
				if (pickedUser != null) {
					// Gets list of credit cards transactions for specific user without date filtering
					for (CreditCard cc : creditCards) {
						List<Transaction> transaction = cc.getTransactions();
						for (Transaction tx : transaction) {
							if (tx.getTransactionCreditCard().getCreditCardUser().getUsername().equals(pickedUser)) {
								transactions.add(tx);
							}
						}
					}
					// Gets list of bank account transactions for specific user without date filtering
					for (Account a : accountList) {
						List<Transaction> transaction = a.getTransactions();
						for (Transaction tx : transaction) {
							if (tx.getTransactionAccount().getAccountUser().getUsername().equals(pickedUser)) {
								transactions.add(tx);
							}
						}
					}
				} else {
					// Gets list of all transactions for credit cards without date filtering
					for (CreditCard cc : creditCards) {
						List<Transaction> transaction = cc.getTransactions();
						for (Transaction tx : transaction) {
							transactions.add(tx);
						}
					}
					// Gets list of all transactions for bank accounts without date filtering
					for (Account a : accountList) {
						List<Transaction> transaction = a.getTransactions();
						for (Transaction tx : transaction) {
							transactions.add(tx);
						}
					}
				}
			}
			else if (pickedType.equals("card")) {
					// Filtering based on selected user
				if (pickedUser != null) {
					// Filtering based on selected user to obtain all credit card transactions without date filtering
					for (CreditCard cc : creditCards) {
						List<Transaction> transaction = cc.getTransactions();
						for (Transaction tx : transaction) {
							if (tx.getTransactionCreditCard().getCreditCardUser().getUsername().equals(pickedUser)) {
								transactions.add(tx);
							}
						}
					}
				} else {
					// Filtering to obtain all credit card transactions without date filtering
					for (CreditCard cc : creditCards) {
						List<Transaction> transaction = cc.getTransactions();
						for (Transaction tx : transaction) {
							transactions.add(tx);
						}
					}
				}
			} else if (pickedType.equals("account")) {
				// Filtering based on bank account
				if (pickedUser != null) {
					// Filtering based on selected user to obtain all bank account transactions without date filtering
					for (Account a : accountList) {
						List<Transaction> transaction = a.getTransactions();
						for (Transaction tx : transaction) {
							if (tx.getTransactionAccount().getAccountUser().getUsername().equals(pickedUser)) {
								transactions.add(tx);
							}
						}
					}
				} else {
					// Filtering to obtain all bank account transactions without date filtering
					for (Account a : accountList) {
						List<Transaction> transaction = a.getTransactions();
						for (Transaction tx : transaction) {
							transactions.add(tx);
						}
					}
				}
			}
		} else {
			// Transactions filtering based on date
			int year = Integer.parseInt(month.substring(0, 4));
			int monthValue = Integer.parseInt(month.substring(5, 7));
			if (pickedType == null) {
				if (pickedUser != null) {
					// Date filtering for credit card transactions based on specified user
					for (CreditCard cc : creditCards) {
						List<Transaction> transaction = cc.getTransactions();
						for (Transaction t : transaction) {
							if ((t.getTransactionDate().getMonthValue() == monthValue
									&& t.getTransactionDate().getYear() == year)
									&& (t.getTransactionCreditCard().getCreditCardUser().getUsername()
											.equals(pickedUser))) {
								transactions.add(t);
							}
						}
					}
					// Date filtering for bank account transactions based on specified user
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
				} else {
					// Date filtering for credit card transactions
					for (CreditCard cc : creditCards) {
						List<Transaction> transaction = cc.getTransactions();
						for (Transaction t : transaction) {
							if (t.getTransactionDate().getMonthValue() == monthValue
									&& t.getTransactionDate().getYear() == year) {
								transactions.add(t);
							}
						}
					}
					// Date filtering for bank account transactions
					for (Account a : accountList) {
						List<Transaction> transaction = a.getTransactions();
						for (Transaction t : transaction) {
							if (t.getTransactionDate().getMonthValue() == monthValue
									&& t.getTransactionDate().getYear() == year) {
								transactions.add(t);
							}
						}
					}
				}
			} else if (pickedType.equals("card")) {
				// Date filtering for credit card transactions for selected user.
				for (CreditCard cc : creditCards) {
					List<Transaction> transaction = cc.getTransactions();
					for (Transaction t : transaction) {
						if ((t.getTransactionDate().getMonthValue() == monthValue
								&& t.getTransactionDate().getYear() == year)
								&& (t.getTransactionCreditCard().getCreditCardUser().getUsername()
										.equals(pickedUser))) {
							transactions.add(t);
						}
					}
				}
			}	// Date filtering for bank account transactions for selected user. 
			else if (pickedType.equals("account")) {
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
			}
		}
		// Final sorting of transactions based on transaction date (earliest to oldest). Add attribute to model for front-end viewing.
		Collections.sort(transactions, Comparator.comparing(Transaction::getTransactionDate));
		model.addAttribute("users", userList);
		model.addAttribute("transactions", transactions);
		return "/admin/admin-transactions";
	}

}
