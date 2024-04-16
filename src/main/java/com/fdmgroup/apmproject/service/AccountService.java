package com.fdmgroup.apmproject.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.repository.AccountRepository;


@Service
public class AccountService {
	@Autowired
	private AccountRepository accountRepo;
	
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
	
	public List<Account> findAllAccountsByUserId(long userId){
		return accountRepo.findByAccountUserUserId(userId);
	}
	
	public List<Account> getAllAccounts(){
		return accountRepo.findAll();
	}
	
	
	public String generateUniqueAccountNumber() {
		StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            sb.append(random.nextInt(10));
            if ((i + 1) % 3 == 0 && i != 8) {
                sb.append("-"); // Adds a dash after every 4 digits, except the last set of 4 digits
            }
        }
        return sb.toString();
	}
	
}
