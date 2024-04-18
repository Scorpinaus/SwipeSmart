package com.fdmgroup.apmproject.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.ForeignExchangeCurrency;
import com.fdmgroup.apmproject.model.Status;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.repository.AccountRepository;
import com.fdmgroup.apmproject.repository.UserRepository;

import jakarta.annotation.PostConstruct;


@Service
public class AccountService {
	
	@Autowired
	private AccountRepository accountRepo;
	@Autowired
	private UserService userService;
	@Autowired
	private StatusService statusService;
	@Autowired
	private ForeignExchangeCurrencyService currencyService;
	
	private static Logger logger = LogManager.getLogger(AccountService.class);
	
	public AccountService(AccountRepository accountRepo) {
		this.accountRepo = accountRepo;
	}
	
	public void persist(Account account) {
		Optional<Account> returnedAccount = accountRepo.findById(account.getAccountId());
		if (returnedAccount.isEmpty()) {
			accountRepo.save(account);
			logger.info("Account successfully created");
		} else {
			logger.warn("Account already exists");
		}
	}
	
	public void update(Account account) {
		Optional<Account> returnedAccount = accountRepo.findById(account.getAccountId());
		if (returnedAccount.isEmpty()) {
			logger.warn("Account does not exist in database");
		} else {
			accountRepo.save(account);
			logger.info("Account successfully updated");
		}
	}
	
	public Account findById(long accountId) {
		Optional<Account> returnedAccount = accountRepo.findById(accountId);
		if (returnedAccount.isEmpty()) {
			logger.warn("Could not find Account in Database");
			return null;
		} else {
			logger.info("Returning Account's details");
			return returnedAccount.get();
		}
	}
	
	public void deleteById(long accountId) {
		Optional<Account> returnedAccount = accountRepo.findById(accountId);
		if (returnedAccount.isEmpty()) {
			logger.warn("Account does not exist in database");
		} else {
			accountRepo.deleteById(accountId);
			logger.info("Account deleted from Database");
		}
	}
	
	//Function that finds a bank account according to account Number.
	public Account findAccountByAccountNumber(String accountNumber) {
		Optional<Account> retrievedAccount = accountRepo.findByAccountNumber(accountNumber);
		if (retrievedAccount.isEmpty()) {
			logger.warn("Bank account does not exist in database");
			return null;
		}
		else {
		Account account = retrievedAccount.get();
		return account;
		}
	}
	
	//Function that calculates new Account Balance.
	public double withdrawAccountByAmount(BigDecimal retrievedAccountBalance, BigDecimal amount) {
		BigDecimal newBalance = retrievedAccountBalance.subtract(amount);
		Double newAccountBalance = newBalance.doubleValue();
		return newAccountBalance;
	}
	
	//Function that finds all Bank Accounts based on user ID
	public List<Account> findAllAccountsByUserId(long userId){
		return accountRepo.findByAccountUserUserId(userId);
	}
	
	//Function that retrieves all Bank Accounts
	public List<Account> getAllAccounts(){
		return accountRepo.findAll();
	}
	
	//Function that generates a unique Bank Account Number when creating a new bank account
	public String generateUniqueAccountNumber() {
	    StringBuilder sb = new StringBuilder();
	    Random random = new Random();
	    
	    do {
	        sb.setLength(0); // Clear the StringBuilder before generating a new account number
	        for (int i = 0; i < 9; i++) {
	            sb.append(random.nextInt(10));
	            if ((i + 1) % 3 == 0 && i != 8) {
	                sb.append("-"); // Adds a dash after every 3 digits, except the last set of 3 digits
	            }
	        }
	    } while (findAccountByAccountNumber(sb.toString()) != null);

	    return sb.toString();
	} 
   
	
	@PostConstruct
	public void intiAccounts() {
		User userJacky = userService.findUserByUsername("jackytan");
		Status statusName = statusService.findByStatusName("Approved");
//<<<<<<< HEAD
		Account account = new Account("Savings", 5000, "123-123-123", userJacky, statusName);
		Account account2 = new Account("Current", 10000, "124-124-124", userJacky, statusName);
		Account accountPending = new Account("pending ac", 10000, "125-125-125", userJacky, statusService.findByStatusName("Pending"));
		Account accountPending2 = new Account("pending ac2", 10123, "126-126-126", userJacky, statusService.findByStatusName("Pending"));
////=======
//		String currencyCode = "SGD";
//		Account account = new Account("Savings", 5000, "123-123-123", userJacky, statusName, currencyCode);
//		Account account2 = new Account("Current", 10000, "124-124-124", userJacky, statusName, currencyCode);
//>>>>>>> 9ef4376cc1629b9dee391399d58bc611f0d358bb
		persist(account);
		persist(account2);
		persist(accountPending);
		persist(accountPending2);
	}
	
	
	
	
}
