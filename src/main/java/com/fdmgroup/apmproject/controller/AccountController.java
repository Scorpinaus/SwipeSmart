package com.fdmgroup.apmproject.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.model.User;

import com.fdmgroup.apmproject.service.AccountService;
import com.fdmgroup.apmproject.service.StatusService;
import com.fdmgroup.apmproject.service.TransactionService;
import com.fdmgroup.apmproject.service.UserService;


import jakarta.servlet.http.HttpSession;

@Controller
public class AccountController {
	
	@Autowired 
	private StatusService statusService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TransactionService transactionService;
		

	private static final Logger LOGGER = LogManager.getLogger(AccountController.class);

	private final int LEASTINITIALDEPOSIT = 5000;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	// Function brings logged on user to BankAccounts page.
	@GetMapping("/bankaccount/dashboard")
	public String showBankAccountDashboard(HttpSession session, Model model) {

		if (session != null && session.getAttribute("loggedUser") != null) {
			// retrieves current user from current session
			User currentUser = (User) session.getAttribute("loggedUser");
			model.addAttribute("user", currentUser);
			//retrieves all active bank accounts under current user
			List<Account> userBankAccounts = accountService.findAllAccountsByUserId(currentUser.getUserId());
			if (userBankAccounts.size() != 0) {
				model.addAttribute("currentUserBankAccounts", userBankAccounts);
				LOGGER.info("User is redirected to bank account dashboard");
				return "accountdashboard";
			} else {
				LOGGER.info("User is redirected to bank account. User has no active bank accounts with the bank");
				model.addAttribute("currentUserBankAccounts", userBankAccounts);
				return "accountdashboard";
			}
		} else {
			return "redirect:/login";
		}

	}

	// Function which brings user to Bank Account withdrawal page
	@GetMapping("/bankaccount/withdrawal")
	public String withdrawalBankAccount(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
		if (session != null && session.getAttribute("loggedUser") != null) {
			User currentUser = (User) session.getAttribute("loggedUser");
			model.addAttribute("user", currentUser);
			List<Account> accounts = accountService.findAllAccountsByUserId(currentUser.getUserId());
			if (accounts.isEmpty()) {
				model.addAttribute("error", "No bank accounts found");
				return "accountdashboard";
			}
			model.addAttribute("accounts", accounts);
			
			// Check for the flash attribute directly in the model. If present, it adds to the current model as true.
		    if (Boolean.TRUE.equals(model.asMap().get("errorInsufficient"))) {
		        model.addAttribute("errorInsufficient", true);
		    }
			return "withdrawal";
		}
		else {
			return "redirect:/login";
		}

	}
	
	//Function which processes the bank account withdrawal request.
	@PostMapping("/bankaccount/withdrawal")
	public String processWithdrawal(@RequestParam("account") long accountId,@RequestParam BigDecimal amount, HttpSession session, RedirectAttributes redirectAttributes) {
		//Checks if user is logged on, if not user will be brought to login page.
		if (session.getAttribute("loggedUser") == null ) {
			return "redirect:/login";
		}
		
		//Retrieve currentUser and selectedAccount for withdrawal
		User currentUser = (User) session.getAttribute("loggedUser");
		Account retrievedAccount = accountService.findById(accountId);
		Double withdrawalAmount = amount.doubleValue();
		BigDecimal retrievedAccountBalance = BigDecimal.valueOf(retrievedAccount.getBalance());
		
		//If balance in account is less than withdrawal amount, user redirected back to withdrawal page + errorInsufficient flash attribute added for subsequent use.
		if (retrievedAccountBalance.compareTo(amount) < 0) {
			LOGGER.info("Bank account id"+ retrievedAccount.getAccountName() + "has insufficient money for withdrawal");
			redirectAttributes.addFlashAttribute("errorInsufficient",true);
			return "redirect:/bankaccount/withdrawal";
		}
		
		 LOGGER.info("Processing withdrawal for account " + retrievedAccount.getAccountNumber());
		    double newAccountBalance = accountService.withdrawAccountByAmount(retrievedAccountBalance, amount);
		    retrievedAccount.setBalance(newAccountBalance);
		    transactionService.persist(new Transaction("withdraw", retrievedAccount, amount.doubleValue(), null, null));
		    accountService.update(retrievedAccount);
		    return "redirect:/bankaccount/dashboard";
	}
	

	@GetMapping("/bankaccount/deposit")
	public String goToDepositPage(Model model, HttpSession session) {

		// Get logged user
		User currentUser = (User) session.getAttribute("loggedUser");

		// Get user id
		long userId = currentUser.getUserId();

		// get all the accounts owned by that user
		List<Account> AccountList = accountService.findAllAccountsByUserId(userId);

		// add user and account list to the model
		model.addAttribute("user", currentUser);
		model.addAttribute("AccountList", AccountList);

		return ("deposit");
	}

	@PostMapping("/bankaccount/deposit")
	public String deposit(@RequestParam("account") long accountId,
			@RequestParam("deposit amount") double depositAmount) {

		// get the required account
		Account accountDeposited = accountService.findById(accountId);

		// Calculate the balance after deposit
		Double updatedBalance = accountDeposited.getBalance() + depositAmount;

		// update the account balance
		accountDeposited.setBalance(updatedBalance);
		accountService.update(accountDeposited);

		
		//Transaction
		double cashback = 0;
		
		Transaction transaction = new Transaction("deposit",depositAmount ,accountDeposited.getAccountNumber(),cashback,null,accountDeposited,null,null );

		transactionService.persist(transaction);
		
		return "redirect:/bankaccount/dashboard";
	}

	@GetMapping("/bankaccount/create")
	public String goToCreateBankAccountPage(HttpSession session, Model model) {
		User loggedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user",loggedUser);
		return "createbankaccount";
	}

	@PostMapping("/bankaccount/create")
	public String createBankAccount(@RequestParam("accountName") String accountName,
			@RequestParam("initialDeposit") double initialDeposit, HttpSession session,
			RedirectAttributes redirectAttributes) {

		if (initialDeposit < LEASTINITIALDEPOSIT) {

			redirectAttributes.addAttribute("InsufficientInitialDepositError", "true");
			LOGGER.info("Insufficient Initial Deposit");
			return "redirect:/bankaccount/create";
		} else if (accountName.matches("\\s*")) {
			LOGGER.info("The account name does not contain any words.");
			return "redirect:/bankaccount/create";
		} else if (accountName.isEmpty()) {
			LOGGER.info("The account name can not be blank");
			return "redirect:/bankaccount/create";
		}
			 else {


			// Get logged user
			User currentUser = (User) session.getAttribute("loggedUser");
			String accountnumber = accountService.generateUniqueAccountNumber();
			Account accountCreated = new Account(accountName, initialDeposit, accountnumber, currentUser, statusService.findByStatusName("Pending") );
			
			//persist new account
			accountService.persist(accountCreated);

			double cashback = 0;			
			Transaction transaction = new Transaction("deposit",initialDeposit,accountCreated.getAccountNumber(),cashback,null,accountCreated,null,null );
			transactionService.persist(transaction);
			LOGGER.info("Bank account number "+ accountCreated.getAccountNumber() + "created");	
			return "redirect:/bankaccount/dashboard";
		}
	}


	@GetMapping("/bankaccount/transfer")
	public String goToTransferPage(Model model, HttpSession session) {

		// Get logged user
		User currentUser = (User) session.getAttribute("loggedUser");

		// Get user id
		long userId = currentUser.getUserId();

		// get all the accounts owned by that user
		List<Account> AccountList = accountService.findAllAccountsByUserId(userId);

		// add user and account list to the model
		model.addAttribute("user", currentUser);
		model.addAttribute("AccountList", AccountList);

		return "transfer";
	}

	@PostMapping("/bankaccount/transfer")
	public String transferMoney(@RequestParam("account") long accountId,
			@RequestParam("transferAmount") Double transferAmount,
			@RequestParam("accountNumberTransferTo") String accountNumberTransferTo, HttpSession session,
			RedirectAttributes redirectAttributes) {

			// get the required accounts
			Account accountFromBalance = accountService.findById(accountId);
			String accountNumber = accountNumberTransferTo.replace(" ","-");
			// validate user is not transferring money to the same account
			if (accountFromBalance.getAccountNumber().equals(accountNumber)) {
	
				redirectAttributes.addAttribute("SameAccountError", "true");
				LOGGER.info("SameAccount");
				return "redirect:/bankaccount/transfer";
				}
			// validate user has sufficient amount in account
			else if (accountFromBalance.getBalance() < transferAmount) {
				redirectAttributes.addAttribute("InsufficientBalanceError", "true");
				LOGGER.info("InsufficientBalance");
				return "redirect:/bankaccount/transfer";
			} else {
			//Check if recipientAccount exists in database. If exists, operate normally, if not, consider one sided transfer.
				Optional<Account> recipientAccount = Optional.ofNullable(accountService.findAccountByAccountNumber(accountNumber));
				//When recipientAccount is internal & existing
				if (!recipientAccount.isEmpty()) {
					// update the accounts' balance
					accountFromBalance.setBalance(accountFromBalance.getBalance() - transferAmount);
					accountService.update(accountFromBalance);
					
					recipientAccount.get().setBalance(recipientAccount.get().getBalance() + transferAmount);
					accountService.update(recipientAccount.get());
					
					// Transaction
//					double cashback = 0;
		
					Transaction internalTransaction = new Transaction("Internal Transfer",accountFromBalance, recipientAccount.get(),transferAmount, recipientAccount.get().getAccountNumber(), null);
		
					transactionService.persist(internalTransaction);
					LOGGER.info("Internal Transfer Success!");
					return "redirect:/bankaccount/dashboard";
					
				} else {
					// update the accounts' balance
					accountFromBalance.setBalance(accountFromBalance.getBalance() - transferAmount);
					accountService.update(accountFromBalance);
					
//					Account externalAccount = new Account();
//					externalAccount.setAccountNumber(accountNumber);
//					externalAccount.setBalance(transferAmount);
					
					//Transactions
//					double cashback = 0;
					Transaction externalTransaction = new Transaction("External Transfer", accountFromBalance, null, transferAmount, accountNumber, null);
					transactionService.persist(externalTransaction);
					LOGGER.info("External Transfer Success!");
					return "redirect:/bankaccount/dashboard";
				}
			}
	} 
}
