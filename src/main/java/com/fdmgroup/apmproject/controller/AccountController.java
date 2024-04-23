package com.fdmgroup.apmproject.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

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
import com.fdmgroup.apmproject.model.ForeignExchangeCurrency;
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.service.AccountService;
import com.fdmgroup.apmproject.service.ForeignExchangeCurrencyService;
import com.fdmgroup.apmproject.service.StatusService;
import com.fdmgroup.apmproject.service.TransactionService;

import jakarta.servlet.http.HttpSession;

/**
 * This class is a Spring MVC controller responsible for handling requests
 * related to bank accounts. It provides methods for displaying account
 * information, performing withdrawals, deposits, transfers, and creating new
 * accounts.
 *
 * @author
 * @version 1.0
 * @since 2024-04-22
 */
@Controller
public class AccountController {

	@Autowired
	private StatusService statusService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private ForeignExchangeCurrencyService currencyService;

	@Autowired
	private TransactionService transactionService;

	private static final Logger LOGGER = LogManager.getLogger(AccountController.class);

	private final int LEASTINITIALDEPOSIT = 5000;
	private List<ForeignExchangeCurrency> currenciesList;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	/**
	 * This method retrieves the logged-in user's bank accounts and displays them on
	 * the "Bank Accounts" page.
	 *
	 * @param session The HTTP session object.
	 * @param model   The model object used to pass data to the view.
	 * @return The name of the view to render.
	 */
	@GetMapping("/bankaccount/dashboard")
	public String showBankAccountDashboard(HttpSession session, Model model) {
		// Checks for null session and if there are active logged-on users. If not
		// logged-on, user will be redirected to log-in page.
		if (session != null && session.getAttribute("loggedUser") != null) {

			// retrieves current user from current session and addAttribute to model for
			// front-end processing.
			User currentUser = (User) session.getAttribute("loggedUser");
			model.addAttribute("user", currentUser);
			// retrieves all active bank accounts under current user
			List<Account> userBankAccounts = accountService.findAllAccountsByUserId(currentUser.getUserId());
			// Checks if the user has active bank accounts, and shows the user their active
			// bank accounts.
			if (userBankAccounts.size() != 0) {
				model.addAttribute("currentUserBankAccounts", userBankAccounts);
				LOGGER.info("User is redirected to bank account dashboard");
				return "account-dashboard";
			} else {
				LOGGER.info("User is redirected to bank account. User has no active bank accounts with the bank");
				model.addAttribute("currentUserBankAccounts", userBankAccounts);
				return "account-dashboard";
			}
		} else {
			return "redirect:/login";
		}
	}

	/**
	 * This method retrieves the logged-in user's bank accounts and displays them on
	 * the "Withdrawal" page.
	 *
	 * @param session            The HTTP session object.
	 * @param model              The model object used to pass data to the view.
	 * @param redirectAttributes The redirect attributes object used to pass error
	 *                           messages to the next request.
	 * @return The name of the view to render.
	 */
	@GetMapping("/bankaccount/withdrawal")
	public String withdrawalBankAccount(HttpSession session, Model model, RedirectAttributes redirectAttributes) {

		// Checks for null session and if there are active logged-on users. If not
		// logged-on, user will be redirected to log-in page.
		if (session != null && session.getAttribute("loggedUser") != null) {

			// retrieves current user from current session and addAttribute to model for
			// front-end processing. Gets list of accounts & checks for created accounts &
			// gets supported currencies list.
			User currentUser = (User) session.getAttribute("loggedUser");
			model.addAttribute("user", currentUser);
			List<Account> accounts = accountService.findAllAccountsByUserId(currentUser.getUserId());
			currenciesList = currencyService.getSupportedCurrencies();

			// Checks for created accounts, where if empty, addAttribute to be shown to user
			// on dashboard page.
			if (accounts.isEmpty()) {
				model.addAttribute("error", "No bank accounts found");
				return "account-dashboard";
			}

			// If user has accounts, both current list of accounts and supported currencies
			// will be added to model for front-end processing.
			model.addAttribute("accounts", accounts);
			LOGGER.info("Currencies List: " + currenciesList);
			model.addAttribute("currencies", currenciesList);

			// Check for the flash attribute directly in the model. If present, it adds to
			// the current model as true. This is for redirect after a user has submitted a
			// withdrawal request.
			if (Boolean.TRUE.equals(model.asMap().get("errorInsufficient"))) {
				model.addAttribute("errorInsufficient", true);
			}
			return "withdrawal";
		} else {
			return "redirect:/login";
		}

	}

	/**
	 * This method handles the withdrawal request by checking the balance, updating
	 * the account balance, and creating a transaction record.
	 *
	 * @param accountId              The ID of the account to withdraw from.
	 * @param withdrawalCurrencyCode The currency code of the withdrawal amount.
	 * @param amount                 The withdrawal amount.
	 * @param session                The HTTP session object.
	 * @param redirectAttributes     The redirect attributes object used to pass
	 *                               error messages to the next request.
	 * @return The name of the view to render.
	 */
	@PostMapping("/bankaccount/withdrawal")
	public String processWithdrawal(@RequestParam("account") long accountId,
			@RequestParam("currency") String withdrawalCurrencyCode, @RequestParam BigDecimal amount,
			HttpSession session, RedirectAttributes redirectAttributes) {
		// Checks if user is logged on, if not user will be brought to login page.
		if (session.getAttribute("loggedUser") == null) {
			return "redirect:/login";
		}
		
		User currentUser = (User) session.getAttribute("loggedUser");
		
		// Retrieve currentUser and selectedAccount for withdrawal
		Account retrievedAccount = accountService.findById(accountId);
		BigDecimal retrievedAccountBalance = BigDecimal.valueOf(retrievedAccount.getBalance());
		String baseCurrencyCode = retrievedAccount.getCurrencyCode();
		LOGGER.info("Withdrawal request: Currency={} Amount={} AccountID={}", withdrawalCurrencyCode, amount,
				accountId);

		// Conversion of withdrawal amount from target currency to base currency if
		// required, where amount is the amount requested in target currency
		BigDecimal adjustedAmount = amount;
		if (!withdrawalCurrencyCode.equals(baseCurrencyCode)) {
			BigDecimal exchangeRate = currencyService.getExchangeRate(withdrawalCurrencyCode, baseCurrencyCode);
			adjustedAmount = amount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP);
			LOGGER.info("Converted amount: {} (Exchange rate: {})", adjustedAmount, exchangeRate);
		}

		// If balance in account is less than withdrawal amount, user redirected back to
		// withdrawal page + errorInsufficient flash attribute added for subsequent use.
		if (retrievedAccountBalance.compareTo(adjustedAmount) < 0) {
			LOGGER.info(
					"Bank account id" + retrievedAccount.getAccountName() + "has insufficient money for withdrawal");
			redirectAttributes.addFlashAttribute("errorInsufficient", true);
			return "redirect:/bankaccount/withdrawal";
		}

		// Assuming sufficient amount, proceed and log withdrawal.
		LOGGER.info("Processing withdrawal for account " + retrievedAccount.getAccountNumber());
		BigDecimal newAccountBalance = retrievedAccountBalance.subtract(adjustedAmount);
		retrievedAccount.setBalance(newAccountBalance.doubleValue());
		Transaction transaction = new Transaction("Withdrawal", retrievedAccount, adjustedAmount.doubleValue(), null,
				currencyService.getCurrencyByCode(withdrawalCurrencyCode), withdrawalCurrencyCode + amount.toString());
		transactionService.persist(transaction);
		retrievedAccount.setTransactions(transaction);
		accountService.update(retrievedAccount);
		return "redirect:/bankaccount/dashboard";
	}

	/**
	 * This method retrieves the logged-in user's bank accounts and displays them on
	 * the "Deposit" page.
	 *
	 * @param model   The model object used to pass data to the view.
	 * @param session The HTTP session object.
	 * @return The name of the view to render.
	 */
	@GetMapping("/bankaccount/deposit")
	public String goToDepositPage(Model model, HttpSession session) {

		// Get logged user
		User currentUser = (User) session.getAttribute("loggedUser");

		// Get user id
		long userId = currentUser.getUserId();

		// get all the accounts owned by that user
		List<Account> AccountList = accountService.findAllAccountsByUserId(userId);
		currenciesList = currencyService.getSupportedCurrencies();

		// add user and account list to the model
		model.addAttribute("user", currentUser);
		model.addAttribute("AccountList", AccountList);
		model.addAttribute("currencies", currenciesList);

		return ("deposit");
	}

	/**
	 * This method handles the deposit request by updating the account balance and
	 * creating a transaction record.
	 *
	 * @param accountId     The ID of the account to deposit into.
	 * @param depositAmount The deposit amount.
	 * @param currencyCode  The currency code of the deposit amount.
	 * @return The name of the view to render.
	 */
	@PostMapping("/bankaccount/deposit")
	public String deposit(@RequestParam("accountId") long accountId,
			@RequestParam("depositAmount") double depositAmount, @RequestParam("currency") String currencyCode) {

		// get the required account
		Account accountDeposited = accountService.findById(accountId);
		ForeignExchangeCurrency accountCurrency = currencyService.getCurrencyByCode(accountDeposited.getCurrencyCode());

		// Get the exchange rate and the converted amount after exchange
		BigDecimal exchangeRate = currencyService.getExchangeRate(currencyCode, accountCurrency.getCode());
		BigDecimal convertedAmount = BigDecimal.valueOf(depositAmount).multiply(exchangeRate);

		// Calculate the balance after deposit
		Double updatedBalance = accountDeposited.getBalance() + convertedAmount.doubleValue();

		// Update the account balance
		accountDeposited.setBalance(updatedBalance);
		accountService.update(accountDeposited);

		// Creates transaction, calculates if there is any cashback and updates
		// transaction and affected account details to database.
		double cashback = 0;

		Transaction transaction = new Transaction("Deposit", accountDeposited, convertedAmount.doubleValue(), null,
				currencyService.getCurrencyByCode(currencyCode), currencyCode + " " + depositAmount);

		transactionService.persist(transaction);
		accountDeposited.setTransactions(transaction);
		accountService.update(accountDeposited);
		return "redirect:/bankaccount/dashboard";
	}

	/**
	 * This method displays the "Create Bank Account" page.
	 *
	 * @param session The HTTP session object.
	 * @param model   The model object used to pass data to the view.
	 * @return The name of the view to render.
	 */
	@GetMapping("/bankaccount/create")
	public String goToCreateBankAccountPage(HttpSession session, Model model) {
		// Retrieves logged on user and adds user details to model for subsequent POST
		// request.
		User loggedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user", loggedUser);
		return "create-bank-account";
	}

	/**
	 * This method creates a new bank account for the logged-in user.
	 *
	 * @param accountName        The name of the new account.
	 * @param initialDeposit     The initial deposit amount.
	 * @param session            The HTTP session object.
	 * @param redirectAttributes The redirect attributes object used to pass error
	 *                           messages to the next request.
	 * @return The name of the view to render.
	 */
	@PostMapping("/bankaccount/create")
	public String createBankAccount(@RequestParam("accountName") String accountName,
			@RequestParam("initialDeposit") double initialDeposit, HttpSession session,
			RedirectAttributes redirectAttributes) {
		// Initial check for deposit amount if its less than specified initial deposit
		// of $5000. If less, user will be redirected back to creation page and shown
		// error. Same applies for empty bank account name.
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
		} else {

			// Retrieve logged on user and creates new account based on local currency
			// (SGD).
			User currentUser = (User) session.getAttribute("loggedUser");
			String accountnumber = accountService.generateUniqueAccountNumber();
			Account accountCreated = new Account(accountName, initialDeposit, accountnumber, currentUser,
					statusService.findByStatusName("Pending"));
			ForeignExchangeCurrency localCurrency = currencyService.getCurrencyByCode("SGD");
			accountCreated.setCurrencyCode(localCurrency.getCode());

			// Creates account entry onto mySQL database.
			accountService.persist(accountCreated);

			// Creating transaction to document initial deposit into newly created bank
			// account, persisting it to database and redirecting user back to account
			// dashboard.
			double cashback = 0;
			Transaction transaction = new Transaction("Initial Deposit", accountCreated, initialDeposit, null,
					localCurrency, localCurrency.getCode() + " " + initialDeposit);
			transactionService.persist(transaction);
			LOGGER.info("Bank account number " + accountCreated.getAccountNumber() + "created");
			return "redirect:/bankaccount/dashboard";
		}
	}

	/**
	 * This method retrieves the logged-in user's bank accounts and displays them on
	 * the "Transfer" page.
	 *
	 * @param model   The model object used to pass data to the view.
	 * @param session The HTTP session object.
	 * @return The name of the view to render.
	 */
	@GetMapping("/bankaccount/transfer")
	public String goToTransferPage(Model model, HttpSession session) {

		// Get logged user
		User currentUser = (User) session.getAttribute("loggedUser");

		// Get user id
		long userId = currentUser.getUserId();

		// get all the accounts owned by that user and supported currencies
		List<Account> AccountList = accountService.findAllAccountsByUserId(userId);
		currenciesList = currencyService.getSupportedCurrencies();

		// add user, account list and currencies list to the model for front-end
		// processing
		model.addAttribute("user", currentUser);
		model.addAttribute("AccountList", AccountList);
		model.addAttribute("currencies", currenciesList);

		return "transfer";
	}

	/**
	 * This method handles the transfer request by checking the balance, updating
	 * the account balances, and creating transaction records.
	 *
	 * @param accountId               The ID of the account to transfer from.
	 * @param transferAmount          The transfer amount.
	 * @param accountNumberTransferTo The account number to transfer to.
	 * @param currencyCode            The currency code of the transfer amount.
	 * @param session                 The HTTP session object.
	 * @param redirectAttributes      The redirect attributes object used to pass
	 *                                error messages to the next request.
	 * @return The name of the view to render.
	 */
	@PostMapping("/bankaccount/transfer")
	public String transferMoney(@RequestParam("account") long accountId,
			@RequestParam("transferAmount") Double transferAmount,
			@RequestParam("accountNumberTransferTo") String accountNumberTransferTo,
			@RequestParam("currency") String currencyCode, HttpSession session, RedirectAttributes redirectAttributes) {

		// Get account to transfer from and the accountNumber for the account to be
		// transferred to
		Account accountFromBalance = accountService.findById(accountId);
		String accountNumber = accountNumberTransferTo.replace(" ", "-");

		// Handle currency conversion
		double exchangeRate = currencyService.getExchangeRate(accountFromBalance.getCurrencyCode(), currencyCode)
				.doubleValue();
		double convertedAmount = transferAmount * exchangeRate;

		// validate user is not transferring money to the same account. If user
		// transferring to same account, user will be redirected and shown error.
		if (accountFromBalance.getAccountNumber().equals(accountNumber)) {
			redirectAttributes.addAttribute("SameAccountError", "true");
			LOGGER.info("Attempted to transfer to the same account");
			return "redirect:/bankaccount/transfer";
		}
		// validate user has sufficient amount in account. If user has insufficient
		// funds, user will be redirected and shown error.
		else if (accountFromBalance.getBalance() < convertedAmount) {
			redirectAttributes.addAttribute("InsufficientBalanceError", "true");
			LOGGER.info("InsufficientBalance");
			return "redirect:/bankaccount/transfer";
		} else {

			// Check if recipientAccount exists in database. If exists, operate normally, if
			// not, consider one sided transfer.
			Optional<Account> recipientAccount = Optional
					.ofNullable(accountService.findAccountByAccountNumber(accountNumber));
			// When recipientAccount is internal & existing
			if (!recipientAccount.isEmpty()
					&& recipientAccount.get().getAccountStatus() == statusService.findByStatusName("Pending")) {

				// validate if account status is approved or not
				redirectAttributes.addAttribute("RecipientAccountPendingError", "true");
				LOGGER.info("Recipient Account is still pending");
				return "redirect:/bankaccount/transfer";

			} else if (!recipientAccount.isEmpty()
					&& recipientAccount.get().getAccountStatus() != statusService.findByStatusName("Pending")) {
				// update the accounts' balance
				accountFromBalance.setBalance(accountFromBalance.getBalance() - convertedAmount);
				accountService.update(accountFromBalance);

				// Update internal account for both recipient and originalAccount
				recipientAccount.get().setBalance(recipientAccount.get().getBalance() + convertedAmount);
				accountService.update(recipientAccount.get());

				// Transaction
				// double cashback = 0;

				// Creates new transaction for account where there are outflow of funds during
				// internal transfer
				Transaction internalTransactionOutflow = new Transaction("Internal Transfer - Outflow",
						accountFromBalance, recipientAccount.get(), convertedAmount,
						recipientAccount.get().getAccountNumber(), currencyService.getCurrencyByCode(currencyCode),
						currencyCode + " " + transferAmount);

				// Creates new transaction for account where there are inflow of funds during
				// internal transfer
				Transaction internalTransactionInflow = new Transaction("Internal Transfer - Inflow",
						recipientAccount.get(), accountFromBalance, convertedAmount,
						accountFromBalance.getAccountNumber(), currencyService.getCurrencyByCode(currencyCode),
						currencyCode + " " + transferAmount);
				// Creating both transactions onto database and logging.
				transactionService.persist(internalTransactionOutflow);
				transactionService.persist(internalTransactionInflow);
				LOGGER.info("Internal Transfer Success!");
				return "redirect:/bankaccount/dashboard";
			} else {
				// Transferred to external account.
				// update the transferee accounts' balance on database
				accountFromBalance.setBalance(accountFromBalance.getBalance() - convertedAmount);
				accountService.update(accountFromBalance);

				// Create transaction for transferee account and persisting it to database. Logs
				// transaction.
//					double cashback = 0;
				Transaction externalTransactionOutflow = new Transaction("External Transfer", accountFromBalance, null,
						convertedAmount, accountNumber, currencyService.getCurrencyByCode(currencyCode),
						currencyCode + " " + transferAmount);
				transactionService.persist(externalTransactionOutflow);
				LOGGER.info("External Transfer Success!");
				return "redirect:/bankaccount/dashboard";
			}
		}
	}
}
