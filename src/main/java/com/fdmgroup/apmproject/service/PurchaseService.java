package com.fdmgroup.apmproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fdmgroup.apmproject.creditCard.restcontroller.PaymentException;
import com.fdmgroup.apmproject.creditCard.restcontroller.PaymentResponse;
import com.fdmgroup.apmproject.creditCard.restcontroller.PurchaseRequest;
import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.repository.AccountRepository;

@Service
public class PurchaseService {
	@Autowired
	private CreditCardService creditCardService;
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AccountService accountService;
	
	 public ResponseEntity<PaymentResponse> purchase(PurchaseRequest request) throws PaymentException {
	        // Validate credit card status, credit limit, and monthly balance
	        CreditCard creditCard = creditCardService.findByCreditCardNumber(request.getCreditCardNumber());
	        

	        // Process the transaction
	        creditCard.setAmountUsed(creditCard.getAmountUsed() + request.getAmount());
	        creditCardService.update(creditCard);

	        // Update the bank account that received the money
	        
	        Account account = accountRepository.findByAccountNumber(request.getAccountNumber()).get();
	        account.setBalance(account.getBalance() + request.getAmount());
	        accountService.update(account);

	        // Return a successful response
	        return ResponseEntity.ok(new PaymentResponse(true, "Transaction completed successfully."));
	    }
}
