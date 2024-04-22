package com.fdmgroup.apmproject.creditCard.restcontroller;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdmgroup.apmproject.controller.AccountController;
import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.MerchantCategoryCode;
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.repository.CreditCardRepository;
import com.fdmgroup.apmproject.repository.MerchantCategoryCodeRepository;
import com.fdmgroup.apmproject.repository.StatusRepository;
import com.fdmgroup.apmproject.service.AccountService;
import com.fdmgroup.apmproject.service.CreditCardService;

@RestController
@RequestMapping("/api/credit-card")
public class PurchaseController {
	@Autowired
	private AccountService accountService;	

	@Autowired
	private CreditCardService creditCardService;

	@Autowired
	private CreditCardRepository creditCardRepository;

	@Autowired
	private StatusRepository statusRepository;
	
	@Autowired
	private MerchantCategoryCodeRepository merchantCategoryCodeRepository;
	
	private static final Logger LOGGER = LogManager.getLogger(AccountController.class);
	//need a dto class
//	@GetMapping("")
//	public List<CreditCard> getAllCreditCard() {
//
//		return creditCardRepository.findAll();
//	}
//
//	@GetMapping("/{creditCardNumber}")
//	public CreditCard getCreditCard(@PathVariable String creditCardNumber) {
//		return creditCardService.findByCreditCardNumber(creditCardNumber);
//	}
//
	
	
	
//	private Boolean valdiateTransaction (PurchaseRequest request) {
//		 if (request.getAccountName() == null || request.getAccountNumber() == null || request.getCreditCardNumber() == null || request.getAmount() == 0 || request.getPin() == null || request.getTransacitionType() == null || request.getMcc() == null || request.getCurrency() == null || request.getDescription() == null) {
//			 throw new PaymentException("All fields are required.");
//		    }
//		
//		
//		CreditCard creditCard = creditCardService.findByCreditCardNumber(request.getCreditCardNumber());
//		if (!(accountService.findAccountByAccountNumber(request.getAccountNumber()).equals(request.getAccountName())))
//			throw new PaymentException("Invalid account or account name");
//		else if (!(creditCard.getPin().equals(request.getPin()))) 
//			throw new PaymentException("Invalid credit card or pin");
//		else
//			return true;
//	}
//	
	
	

	
	@PostMapping("purchase")
	public ResponseEntity<PaymentResponse> purchase(@RequestBody PurchaseRequest request) {
		try {
			if (request.getAccountName() == null || request.getAccountNumber() == null || request.getCreditCardNumber() == null || request.getAmount() == 0 || request.getPin() == null || request.getMcc() == null || request.getCurrency() == null || request.getDescription() == null) {
				LOGGER.info("All fields are required.");
				return ResponseEntity.badRequest().body(new PaymentResponse(false, "All fields are required."));
		    }

		    // Validate credit card status, credit limit, and monthly balance
		    CreditCard creditCard = creditCardService.findByCreditCardNumber(request.getCreditCardNumber());
		    if (creditCard == null) {
		    	LOGGER.info("Invalid credit card number.");
		        return ResponseEntity.badRequest().body(new PaymentResponse(false, "Invalid credit card number."));
		    }
		    
		    else if (!creditCard.getCreditCardStatus().equals(statusRepository.findByStatusName("Approved").get())) {
		    	LOGGER.info("Credit card is not active.");
		    	return ResponseEntity.badRequest().body(new PaymentResponse(false, "Credit card is not active."));
		    }
		    
		    else if (creditCard.getCardLimit() < request.getAmount()) {
		    	LOGGER.info("Insufficient credit limit.");
		    	return ResponseEntity.badRequest().body(new PaymentResponse(false, "Insufficient credit limit."));
		    }
		    
		    else if ((creditCard.getCardLimit()-creditCard.getAmountUsed()) < request.getAmount()) {
		    	LOGGER.info("Insufficient credit available.");
		    	return ResponseEntity.badRequest().body(new PaymentResponse(false, "Insufficient credit available."));
		    }
		    else if (!(creditCard.getPin().equals(request.getPin()))) {
				
		    	LOGGER.info("Invalid PIN.");
		    	return ResponseEntity.badRequest().body(new PaymentResponse(false, "Invalid PIN."));
			}else {
				// process transaction
				
				System.out.print("purchase");
				System.out.print(request.toString());
				Optional<MerchantCategoryCode> transactionMerchantCategoryCode = merchantCategoryCodeRepository.findByMerchantCategory(request.getMcc());
				String currencyCode = request.getCurrency();
				
				//modify here
				//update the transaction here! thanks
				

				
				
				
				
				
				
				
				
				return ResponseEntity.ok(new PaymentResponse(true, "Transaction completed successfully."));
			}
			

			
		} catch (PaymentException e) {
			System.out.print("error");
			return ResponseEntity.badRequest().body(new PaymentResponse(false, e.getMessage()));
		}
	}
}
