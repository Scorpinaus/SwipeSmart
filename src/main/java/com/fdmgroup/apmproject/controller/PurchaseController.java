package com.fdmgroup.apmproject.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.ForeignExchangeCurrency;
import com.fdmgroup.apmproject.model.MerchantCategoryCode;
import com.fdmgroup.apmproject.model.PaymentException;
import com.fdmgroup.apmproject.model.PaymentResponse;
import com.fdmgroup.apmproject.model.PurchaseRequest;
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.repository.MerchantCategoryCodeRepository;
import com.fdmgroup.apmproject.repository.StatusRepository;
import com.fdmgroup.apmproject.service.AccountService;
import com.fdmgroup.apmproject.service.CreditCardService;
import com.fdmgroup.apmproject.service.ForeignExchangeCurrencyService;
import com.fdmgroup.apmproject.service.PurchaseService;
import com.fdmgroup.apmproject.service.TransactionService;
import com.fdmgroup.apmproject.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * This class is a REST controller that handles purchase requests for credit
 * cards. It provides an endpoint for initiating a purchase transaction using a
 * credit card.
 *
 * @author
 * @version 1.0
 * @since 2024-04-22
 */
@RestController
@RequestMapping("/api/credit-card")
public class PurchaseController {
	@Autowired
	private AccountService accountService;

	@Autowired
	private CreditCardService creditCardService;

	@Autowired
	private StatusRepository statusRepository;

	@Autowired
	private MerchantCategoryCodeRepository merchantCategoryCodeRepository;

	@Autowired
	private PurchaseService purchaseService;

	@Autowired
	private UserService userService;

	@Autowired
	private ForeignExchangeCurrencyService foreignExchangeCurrencyService;

	@Autowired
	private TransactionService transactionService;

	private static final Logger LOGGER = LogManager.getLogger(AccountController.class);

	@GetMapping("/purchase")
	public String purchase() {
		return "purchase";
	}

	// can test with this json
//	{
//    "accountName" : "Current",
//    "accountNumber" : "124-124-124",
//    "creditCardNumber" : "1234-5678-1234-5678",
//    "amount" : 123.00,
//    "pin" : "123",
//    "mcc" : "Shopping",
//    "currency" : "USD",
//    "description" : "purchase simulation"
//	}

	/**
	 * Handles a purchase request for a credit card.
	 *
	 * @param request The purchase request containing the necessary details.
	 * @return A response entity containing the result of the purchase transaction.
	 */
	@PostMapping("/purchase")
	public ResponseEntity<PaymentResponse> purchase(@RequestBody PurchaseRequest request, HttpSession session) {
		try {
			// Get the exchange rate and the converted amount after exchange
			BigDecimal exchangeRate = foreignExchangeCurrencyService.getExchangeRate(request.getCurrency(),
					accountService.findAccountByAccountNumber(request.getAccountNumber()).getCurrencyCode());
			BigDecimal convertedAmount = BigDecimal.valueOf(request.getAmount()).multiply(exchangeRate);
			request.setAmount(convertedAmount.doubleValue());

			// validation
			if (request.getAccountName() == null || request.getAccountNumber() == null
					|| request.getCreditCardNumber() == null || request.getAmount() == 0 || request.getPin() == null
					|| request.getMcc() == null || request.getCurrency() == null || request.getDescription() == null) {
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

			else if ((creditCard.getCardLimit() - creditCard.getAmountUsed()) < request.getAmount()) {
				LOGGER.info("Insufficient credit available.");
				return ResponseEntity.badRequest().body(new PaymentResponse(false, "Insufficient credit available."));
			} else if (!(creditCard.getPin().equals(request.getPin()))) {

				LOGGER.info("Invalid PIN.");
				return ResponseEntity.badRequest().body(new PaymentResponse(false, "Invalid PIN."));
			} else {

				// process transaction
				Optional<MerchantCategoryCode> transactionMerchantCategoryCode = merchantCategoryCodeRepository
						.findByMerchantCategory(request.getMcc());
				ForeignExchangeCurrency foreignExchangeCurrency = foreignExchangeCurrencyService
						.getCurrencyByCode(request.getCurrency());

				// record transaction
				Transaction transaction = new Transaction("CC Purchase", request.getAmount(), null, 0, creditCard,
						accountService.findAccountByAccountNumber(request.getAccountNumber()),
						transactionMerchantCategoryCode.get(), foreignExchangeCurrency);
				transaction.setCreditCardDescription(request.getDescription(), exchangeRate.doubleValue());
				transactionService.persist(transaction);
				transactionService.updateCreditCardBalance(transaction);

				// update creditcard and account
				purchaseService.purchase(request);
				User currentUser = creditCard.getCreditCardUser();
				List<CreditCard> userCreditCards = currentUser.getCreditCards();
				List<CreditCard> newUserCreditCards = new ArrayList<>();
				for (CreditCard c : userCreditCards) {
					if (c != creditCard) {
						newUserCreditCards.add(c);
					}

				}

				// Replaces current creditcard entity with updated credit card entity, updates
				// user and their avaliable credit card list. Sorts before redirecting user back
				// to credit card dashboard page.
				newUserCreditCards.add(creditCard);
				Collections.sort(newUserCreditCards, Comparator.comparing(CreditCard::getCreditCardId));
				currentUser.setCreditCardList(newUserCreditCards);
				userService.update(currentUser);
				session.setAttribute("loggedUser", currentUser);

				return ResponseEntity.ok(new PaymentResponse(true, "Transaction completed successfully."));
			}

		} catch (PaymentException e) {
			System.out.print("error");
			return ResponseEntity.badRequest().body(new PaymentResponse(false, e.getMessage()));
		}
	}
}
