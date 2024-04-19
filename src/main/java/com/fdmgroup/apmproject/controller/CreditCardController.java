package com.fdmgroup.apmproject.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.ForeignExchangeCurrency;
import com.fdmgroup.apmproject.model.MerchantCategoryCode;
import com.fdmgroup.apmproject.model.Status;
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.service.CreditCardService;
import com.fdmgroup.apmproject.service.ForeignExchangeCurrencyService;
import com.fdmgroup.apmproject.service.MerchantCategoryCodeService;
import com.fdmgroup.apmproject.service.StatusService;
import com.fdmgroup.apmproject.service.TransactionService;
import com.fdmgroup.apmproject.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CreditCardController {

	@Autowired
	private CreditCardService creditCardService;

	@Autowired
	private ForeignExchangeCurrencyService currencyService;

	@Autowired
	private StatusService statusService;
	@Autowired
	private MerchantCategoryCodeService merchantCategoryCodeService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private UserService userService;
	private static Logger logger = LogManager.getLogger(CreditCardController.class);
	private List<ForeignExchangeCurrency> currencies;

	@GetMapping("/userCards")
	public String viewCreditCards(Model model, HttpSession session) {
		if (session != null && session.getAttribute("loggedUser") != null) {
			User loggedUser = (User) session.getAttribute("loggedUser");
			List<CreditCard> userCreditCards = loggedUser.getCreditCards();
			model.addAttribute("cards", userCreditCards);
			model.addAttribute("user", loggedUser);
			Collections.sort(userCreditCards, Comparator.comparing(CreditCard::getCreditCardId));
			return "card-dashboard";
		} else {
			model.addAttribute("error", true);
			logger.warn("User Is not logged-in. Please login first");
			return "card-dashboard";
		}

	}

	@GetMapping("/applyCreditCard")
	public String applyCreditCard(Model model, HttpSession session) {
		if (session != null && session.getAttribute("loggedUser") != null) {
			User loggedUser = (User) session.getAttribute("loggedUser");
			model.addAttribute("user", loggedUser);
			return "apply-credit-card";
		} else {
			return "apply-credit-card";
		}
	}

	@PostMapping("/applyCreditCard")
	public String applyCreditCard(Model model, HttpSession session, @RequestParam String monthlySalary,
			@RequestParam String cardType) {
		User loggedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user", loggedUser);
		if (monthlySalary.isBlank() || cardType.isBlank()) {
			logger.warn("There are empty fields, please fill up");
			model.addAttribute("error", true);
			return "apply-credit-card";
		} else {
			if (Double.parseDouble(monthlySalary) < 1000) {
				logger.warn("Your salary is too low. You are required to have a monthly salary above $1000");
				model.addAttribute("error2", true);
				return "apply-credit-card";
			} else {
				String creditCardNumber = creditCardService.generateCreditCardNumber();
				String pin = creditCardService.generatePinNumber();
				double cardLimit = Double.parseDouble(monthlySalary) * 3;
				// Default approved

				ForeignExchangeCurrency localCurrency = currencyService.getCurrencyByCode("SGD");

				// set credit card as pending when they apply for the card
				Status statusName = statusService.findByStatusName("Pending");

//				//set credit card as Approved when they apply for the card
//				Status statusName = statusService.findByStatusName("Approved");

				CreditCard createCreditCard = new CreditCard(creditCardNumber, pin, cardLimit, cardType, statusName, 0,
						loggedUser, localCurrency.getCode());
				creditCardService.persist(createCreditCard);
				logger.info("Credit card of number " + creditCardNumber + " created");
				loggedUser.setCreditCards(createCreditCard);
				userService.update(loggedUser);

				return "redirect:/userCards";
			}
		}
	}

	@GetMapping("/creditCard/paybills")
	public String paybills(Model model, HttpSession session) {
		if (session != null && session.getAttribute("loggedUser") != null) {
			// Get logged user
			User currentUser = (User) session.getAttribute("loggedUser");
			List<CreditCard> ccList = currentUser.getCreditCards();

			// add user and account list to the model
			model.addAttribute("user", currentUser);
			model.addAttribute("CcList", ccList);

			return "pay-bills";
		} else {
			model.addAttribute("error", true);
			logger.warn("User Is not logged-in. Please login first");
			return "login";
		}
	}

	@PostMapping("/creditCard/paybills")
	public String makeCcbills(Model model, HttpSession session, @RequestParam("creditCard") Long creditCardId,
			@RequestParam(name = "payment", required = false) Double paymentAmount,
			@RequestParam("balanceType") String balanceType) {

		// Get logged user
		User currentUser = (User) session.getAttribute("loggedUser");
		CreditCard creditCard = creditCardService.findById(creditCardId);
		MerchantCategoryCode mccBill = merchantCategoryCodeService.findByMerchantCategory("Bill");
		Transaction transaction = null;
		ForeignExchangeCurrency currency = currencyService.getCurrencyByCode("SGD");

		if (balanceType.equals("custom")) {
			transaction = new Transaction("CC Bill Payment", paymentAmount, null, 0.00, creditCard, null, mccBill,
					currency);
		} else if (balanceType.equals("minimum")) {
			if (creditCard.getMonthlyBalance() < 50)
				transaction = new Transaction("CC Bill Payment", creditCard.getMonthlyBalance(), null, 0.00, creditCard,
						null, mccBill, currency);
			else
				transaction = new Transaction("CC Bill Payment", 50, null, 0.00, creditCard, null, mccBill, null);
		} else if (balanceType.equals("statement")) {
			transaction = new Transaction("CC Bill Payment", creditCard.getMonthlyBalance(), null, 0.00, creditCard,
					null, mccBill, currency);
		} else if (balanceType.equals("current")) {
			transaction = new Transaction("CC Bill Payment", creditCard.getAmountUsed(), null, 0.00, creditCard, null,
					mccBill, currency);
		}

		transactionService.persist(transaction);
		transactionService.updateCreditCardBalance(transaction);

		logger.info("Payment of" + balanceType + " balance to credit card " + creditCard.getCreditCardNumber()
				+ " completed");

		List<CreditCard> userCreditCards = currentUser.getCreditCards();
		List<CreditCard> newUserCreditCards = new ArrayList<>();
		for (CreditCard c : userCreditCards) {
			if (c.getCreditCardId() != creditCardId) {
				newUserCreditCards.add(c);
			}
		}
		newUserCreditCards.add(creditCard);
		currentUser.setCreditCardList(newUserCreditCards);
		userService.update(currentUser);
		session.setAttribute("loggedUser", currentUser);
		Collections.sort(newUserCreditCards, Comparator.comparing(CreditCard::getCreditCardId));
		return "redirect:/userCards";
	}

}
