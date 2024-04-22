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
	private ForeignExchangeCurrencyService currencyService;

	@Autowired
	private TransactionService transactionService;

	private static final Logger LOGGER = LogManager.getLogger(AccountController.class);

	private final int LEASTINITIALDEPOSIT = 5000;
	private List<ForeignExchangeCurrency> currenciesList;

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
			// retrieves all active bank accounts under current user
			List<Account> userBankAccounts = accountService.findAllAccountsByUserId(currentUser.getUserId());
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

	// Function which brings user to Bank Account withdrawal page
	@GetMapping("/bankaccount/withdrawal")
	public String withdrawalBankAccount(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
		if (session != null && session.getAttribute("loggedUser") != null) {
			User currentUser = (User) session.getAttribute("loggedUser");
			model.addAttribute("user", currentUser);
			List<Account> accounts = accountService.findAllAccountsByUserId(currentUser.getUserId());
			currenciesList = currencyService.getSupportedCurrencies();
			if (accounts.isEmpty()) {
				model.addAttribute("error", "No bank accounts found");
				return "account-dashboard";
			}
			model.addAttribute("accounts", accounts);
			LOGGER.info("Currencies List: " + currenciesList);
			model.addAttribute("currencies", currenciesList);
			// Check for the flash attribute directly in the model. If present, it adds to
			// the current model as true.
			if (Boolean.TRUE.equals(model.asMap().get("errorInsufficient"))) {
				model.addAttribute("errorInsufficient", true);
			}
			return "withdrawal";
		} else {
			return "redirect:/login";
		}

	}

	// Function which processes the bank account withdrawal request.
	@PostMapping("/bankaccount/withdrawal")
	public String processWithdrawal(@RequestParam("account") long accountId,
			@RequestParam("currency") String withdrawalCurrencyCode, @RequestParam BigDecimal amount,
			HttpSession session, RedirectAttributes redirectAttributes) {
		// Checks if user is logged on, if not user will be brought to login page.
		if (session.getAttribute("loggedUser") == null) {
			return "redirect:/login";
		}

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

	@PostMapping("/bankaccount/deposit")
	public String deposit(@RequestParam("accountId") long accountId, @RequestParam("depositAmount") double depositAmount,
			@RequestParam("currency") String currencyCode) {

		// get the required account
		Account accountDeposited = accountService.findById(accountId);
		ForeignExchangeCurrency accountCurrency = currencyService.getCurrencyByCode(accountDeposited.getCurrencyCode());

		// Get the exchange rate and the converted amount after exchange
		BigDecimal exchangeRate = currencyService.getExchangeRate(currencyCode, accountCurrency.getCode());
		BigDecimal convertedAmount = BigDecimal.valueOf(depositAmount).multiply(exchangeRate);

		// Calculate the balance after deposit
		Double updatedBalance = accountDeposited.getBalance() + convertedAmount.doubleValue();

		// update the account balance
		accountDeposited.setBalance(updatedBalance);
		accountService.update(accountDeposited);

		// Transaction
		double cashback = 0;

		Transaction transaction = new Transaction("Deposit", accountDeposited, convertedAmount.doubleValue(), null, currencyService.getCurrencyByCode(currencyCode), currencyCode + " " + depositAmount);

		transactionService.persist(transaction);
		accountDeposited.setTransactions(transaction);
		accountService.update(accountDeposited);
		return "redirect:/bankaccount/dashboard";
	}

	@GetMapping("/bankaccount/create")
	public String goToCreateBankAccountPage(HttpSession session, Model model) {
		User loggedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user", loggedUser);
		return "create-bank-account";
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
		} else {

			// Get logged user
			User currentUser = (User) session.getAttribute("loggedUser");
			String accountnumber = accountService.generateUniqueAccountNumber();
			Account accountCreated = new Account(accountName, initialDeposit, accountnumber, currentUser,
					statusService.findByStatusName("Pending"));
			ForeignExchangeCurrency localCurrency = currencyService.getCurrencyByCode("SGD");
			accountCreated.setCurrencyCode(localCurrency.getCode());

			// persist new account
			accountService.persist(accountCreated);

			double cashback = 0;
			Transaction transaction = new Transaction("Initial Deposit", accountCreated, initialDeposit, null, localCurrency, localCurrency.getCode() + " " + initialDeposit );
			transactionService.persist(transaction);
			LOGGER.info("Bank account number " + accountCreated.getAccountNumber() + "created");
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
		currenciesList = currencyService.getSupportedCurrencies();

		// add user and account list to the model
		model.addAttribute("user", currentUser);
		model.addAttribute("AccountList", AccountList);
		model.addAttribute("currencies", currenciesList);

		return "transfer";
	}

	@PostMapping("/bankaccount/transfer")
	public String transferMoney(@RequestParam("account") long accountId,
			@RequestParam("transferAmount") Double transferAmount,
			@RequestParam("accountNumberTransferTo") String accountNumberTransferTo,
			@RequestParam("currency") String currencyCode, HttpSession session, RedirectAttributes redirectAttributes) {

		// get the required accounts
		Account accountFromBalance = accountService.findById(accountId);
		String accountNumber = accountNumberTransferTo.replace(" ", "-");

		// Handle currency conversion
		double exchangeRate = currencyService.getExchangeRate(accountFromBalance.getCurrencyCode(), currencyCode)
				.doubleValue();
		double convertedAmount = transferAmount * exchangeRate;

		// validate user is not transferring money to the same account
		if (accountFromBalance.getAccountNumber().equals(accountNumber)) {
			redirectAttributes.addAttribute("SameAccountError", "true");
			LOGGER.info("Attempted to transfer to the same account");
			return "redirect:/bankaccount/transfer";
		}
		// validate user has sufficient amount in account
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

				// For account which funds are flowing out of during internal transfer
				Transaction internalTransactionOutflow = new Transaction("Internal Transfer - Outflow",
						accountFromBalance, recipientAccount.get(), convertedAmount,
						recipientAccount.get().getAccountNumber(), currencyService.getCurrencyByCode(currencyCode), currencyCode + " " + transferAmount);

				// For account which funds are flowing into during internal transfer
				Transaction internalTransactionInflow = new Transaction("Internal Transfer - Inflow",
						recipientAccount.get(), accountFromBalance, convertedAmount,
						accountFromBalance.getAccountNumber(), currencyService.getCurrencyByCode(currencyCode), currencyCode + " " + transferAmount);

				transactionService.persist(internalTransactionOutflow);
				transactionService.persist(internalTransactionInflow);
				LOGGER.info("Internal Transfer Success!");
				return "redirect:/bankaccount/dashboard";
			} else {
				// Transferred to external account.
				// update the accounts' balance
				accountFromBalance.setBalance(accountFromBalance.getBalance() - convertedAmount);
				accountService.update(accountFromBalance);

				// Transactions
//					double cashback = 0;
				Transaction externalTransactionOutflow = new Transaction("External Transfer", accountFromBalance, null,
						convertedAmount, accountNumber, currencyService.getCurrencyByCode(currencyCode), currencyCode + " " + transferAmount);
				transactionService.persist(externalTransactionOutflow);
				LOGGER.info("External Transfer Success!");
				return "redirect:/bankaccount/dashboard";
			}
		}
	}
}
