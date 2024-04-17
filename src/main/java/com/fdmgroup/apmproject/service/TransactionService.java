package com.fdmgroup.apmproject.service;

import java.time.LocalDateTime;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.MerchantCategoryCode;
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.repository.TransactionRepository;

import jakarta.annotation.PostConstruct;

@Service
public class TransactionService {
	@Autowired
	private TransactionRepository transactionRepo;
	
	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private MerchantCategoryCodeService merchantCategoryCodeService;
	
	private static Logger logger = LogManager.getLogger(TransactionService.class);
	
	public TransactionService(TransactionRepository transactionRepo) {
		this.transactionRepo = transactionRepo;
	}
	
	public void persist(Transaction transaction) {
		Optional<Transaction> returnedTransaction = transactionRepo.findById(transaction.getTransactionId());
		if (returnedTransaction.isEmpty()) {
			transactionRepo.save(transaction);
			logger.info("Transaction successfully created");
		} else {
			logger.warn("Transaction already exists");
		}
	}
	
	public void update(Transaction transaction) {
		Optional<Transaction> returnedTransaction = transactionRepo.findById(transaction.getTransactionId());
		if (returnedTransaction.isEmpty()) {
			logger.warn("Transaction does not exist in database");
		} else {
			transactionRepo.save(transaction);
			logger.info("Transaction successfully updated");
		}
	}
	
	public Transaction findById(long transactionId) {
		Optional<Transaction> returnedTransaction = transactionRepo.findById(transactionId);
		if (returnedTransaction.isEmpty()) {
			logger.warn("Could not find Transaction in Database");
			return null;
		} else {
			logger.info("Returning Transaction's details");
			return returnedTransaction.get();
		}
	}
	
	public void deleteById(long transactionId) {
		Optional<Transaction> returnedTransaction = transactionRepo.findById(transactionId);
		if (returnedTransaction.isEmpty()) {
			logger.warn("Transaction does not exist in database");
		} else {
			transactionRepo.deleteById(transactionId);
			logger.info("Transaction deleted from Database");
		}
	}
	
	public void updateCreditCardBalance(Transaction transaction) {
		// ensure amount used in credit card is updated
		if (transaction.getTransactionCreditCard() != null && transaction.getTransactionType().equals("Withdraw")) {
			CreditCard creditCard = transaction.getTransactionCreditCard();
			creditCard.addTransaction(transaction.getTransactionAmount());
			creditCardService.update(creditCard);
		}
	}
	
	public List<Transaction> getTransactionsByMonthAndYearAndTransactionAccount(int year, int monthValue, Account account) {
        YearMonth yearMonth = YearMonth.of(year, monthValue);
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        return transactionRepo.findByTransactionDateBetweenAndTransactionAccount(startOfMonth,endOfMonth,account);
    }
	
	public List<Transaction> getTransactionsByMonthAndYearAndTransactionCreditCard(int year, int monthValue, CreditCard creditcard) {
        YearMonth yearMonth = YearMonth.of(year, monthValue);
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        return transactionRepo.findByTransactionDateBetweenAndTransactionCreditCard(startOfMonth,endOfMonth,creditcard);
    }
	
	public List<Transaction> findByTransactionAccountOrRecipientAccount(Account transactionAccount, Account recipientAccount){
		List<Transaction> Transactions = new ArrayList<>();
		
		Transactions = transactionRepo.findByTransactionAccountOrRecipientAccount(transactionAccount, recipientAccount);
	
	for (Transaction transaction : Transactions) {
		
	
	    String accountFromNumber = transaction.getTransactionAccount().getAccountNumber();
	    String maskedAccountFromNumber = "***-***-" + accountFromNumber.substring(accountFromNumber.length() - 3);
	    transaction.getTransactionAccount().setAccountNumber(maskedAccountFromNumber);
	    
	    String accountToNumber = transaction.getRecipientAccount().getAccountNumber();
	    String maskedAccountToNumber = "***-***-" + accountToNumber.substring(accountToNumber.length() - 3);
	    transaction.getRecipientAccount().setAccountNumber(maskedAccountToNumber);
	    
	}
	    // Repeat the same for the recipient account if necessary
	    
	    return Transactions;
	}
	
	
	@PostConstruct
	public void initTransactions() {
		CreditCard creditCard = creditCardService.findByCreditCardNumber("1234-5678-1234-5678");
		MerchantCategoryCode mcc = merchantCategoryCodeService.findByMerchantCategory("Dining");
		MerchantCategoryCode mcc1 = merchantCategoryCodeService.findByMerchantCategory("Shopping");
		MerchantCategoryCode mcc2 = merchantCategoryCodeService.findByMerchantCategory("Travel");
		Transaction transaction = new Transaction(LocalDateTime.of(2024, 4, 15, 12, 34, 56), "Withdraw",20.5,null,0.00, creditCard,null,mcc,null);
		Transaction transaction1 = new Transaction(LocalDateTime.of(2024, 4, 12, 12, 34, 56), "Withdraw",10,null,0.00, creditCard,null,mcc,null);
		Transaction transaction2 = new Transaction(LocalDateTime.of(2024, 3, 12, 11, 33, 56), "Withdraw",10,null,0.00, creditCard,null,mcc1,null);
		Transaction transaction3 = new Transaction(LocalDateTime.of(2024, 3, 28, 11, 33, 56), "Withdraw",50,null,0.00, creditCard,null,mcc2,null);
		Transaction transaction4 = new Transaction(LocalDateTime.of(2024, 3, 9, 11, 33, 56), "Withdraw",150.10,null,0.00, creditCard,null,mcc2,null);
		Transaction transaction5 = new Transaction(LocalDateTime.of(2024, 2, 12, 11, 33, 56), "Withdraw",20,null,0.00, creditCard,null,mcc,null);
		persist(transaction);
		persist(transaction1);
		persist(transaction2);
		persist(transaction3);
		persist(transaction4);
		persist(transaction5);
		updateCreditCardBalance(transaction);
		updateCreditCardBalance(transaction1);
		updateCreditCardBalance(transaction2);
		updateCreditCardBalance(transaction3);
		updateCreditCardBalance(transaction4);
		updateCreditCardBalance(transaction5);
	}

}
