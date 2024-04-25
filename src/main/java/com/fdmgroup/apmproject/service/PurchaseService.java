package com.fdmgroup.apmproject.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.PaymentException;
import com.fdmgroup.apmproject.model.PaymentResponse;
import com.fdmgroup.apmproject.model.PurchaseRequest;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.repository.AccountRepository;
import com.fdmgroup.apmproject.repository.UserRepository;

/**
 * This class is responsible for handling all business logic related to
 * purchases.
 * 
 * @author
 * @version 1.0
 * @since 2024-04-22
 */
@Service
public class PurchaseService {
	@Autowired
	private CreditCardService creditCardService;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountService accountService;
	@Autowired
	private UserService userService;

	/**
	 * Processes a purchase request using a specified credit card and credits the
	 * recipient's account.
	 * <p>
	 * This method validates the credit card's status, credit limit, and available
	 * balance before proceeding with the transaction. If the card is valid, the
	 * purchase amount is added to the card's used amount and the corresponding
	 * account is credited. The method ensures that all financial adjustments are
	 * recorded accurately and returns a response indicating the success of the
	 * transaction.
	 *
	 * @param request The PurchaseRequest containing details such as the credit card
	 *                number, account number, and transaction amount.
	 * @return ResponseEntity<PaymentResponse> containing the transaction status and
	 *         a message indicating success or failure.
	 * @throws PaymentException If the transaction cannot be processed due to issues
	 *                          with the credit card or account, such as exceeding
	 *                          credit limits or invalid card status.
	 * @see CreditCardService#findByCreditCardNumber(String) For retrieving and
	 *      validating credit card details.
	 * @see AccountService#update(Account) For updating the account balance after
	 *      receiving funds.
	 */
	public ResponseEntity<PaymentResponse> purchase(PurchaseRequest request) throws PaymentException {
		// Validate credit card status, credit limit, and monthly balance
		CreditCard creditCard = creditCardService.findByCreditCardNumber(request.getCreditCardNumber());

		// Update the user

		User user = userRepository.findByUsername(creditCard.getCreditCardUser().getUsername()).get();
		List<CreditCard> userCreditCards = user.getCreditCards();
		List<CreditCard> newUserCreditCards = new ArrayList<>();
		for (CreditCard c : userCreditCards) {
			if (c.getCreditCardNumber() != creditCard.getCreditCardNumber()) {
				newUserCreditCards.add(c);
			}

		}
		newUserCreditCards.add(creditCard);
		user.setCreditCardList(newUserCreditCards);
		userService.update(user);
		// Return a successful response
		return ResponseEntity.ok(new PaymentResponse(true, "Transaction completed successfully."));
	}
}
