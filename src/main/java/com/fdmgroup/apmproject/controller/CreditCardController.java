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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.ForeignExchangeCurrency;
import com.fdmgroup.apmproject.model.MerchantCategoryCode;
import com.fdmgroup.apmproject.model.Status;
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.service.AccountService;
import com.fdmgroup.apmproject.service.CreditCardService;
import com.fdmgroup.apmproject.service.ForeignExchangeCurrencyService;
import com.fdmgroup.apmproject.service.MerchantCategoryCodeService;
import com.fdmgroup.apmproject.service.StatusService;
import com.fdmgroup.apmproject.service.TransactionService;
import com.fdmgroup.apmproject.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * This class is a Spring MVC controller responsible for handling requests related to credit cards.
 * It provides methods for viewing, applying, and paying bills for credit cards.
 *
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */
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
	
	@Autowired
	private AccountService accountService;
	
	private static Logger logger = LogManager.getLogger(CreditCardController.class);
	private List<ForeignExchangeCurrency> currencies;

	public CreditCardController() {
		
	}

	public CreditCardController(CreditCardService creditCardService, ForeignExchangeCurrencyService currencyService,
			StatusService statusService, UserService userService, AccountService accountService, MerchantCategoryCodeService merchantCategoryCodeService, TransactionService transactionService) {
		this.creditCardService = creditCardService;
		this.currencyService = currencyService;
		this.statusService = statusService;
		this.userService = userService;
		this.accountService = accountService;
		this.merchantCategoryCodeService = merchantCategoryCodeService;
		this.transactionService = transactionService;
	}
	
	/**
     * This method displays a list of credit cards associated with the logged-in user.
     *
     * @param model  The model to be used for rendering the view.
     * @param session The HTTP session containing the logged-in user information.
     * @return The name of the view to be rendered.
     */
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
	
	 /**
     * This method displays a form for applying for a new credit card.
     *
     * @param model  The model to be used for rendering the view.
     * @param session The HTTP session containing the logged-in user information.
     * @return The name of the view to be rendered.
     */
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
	
	/**
     * This method handles the submission of a credit card application form.
     *
     * @param model        The model to be used for rendering the view.
     * @param session      The HTTP session containing the logged-in user information.
     * @param monthlySalary The applicant's monthly salary.
     * @param cardType     The type of credit card being applied for.
     * @return The name of the view to be rendered.
     */
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
				

				ForeignExchangeCurrency localCurrency = currencyService.getCurrencyByCode("SGD");

				// set credit card as pending when they apply for the card
				Status statusName = statusService.findByStatusName("Pending");

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
     
	 /**
	  * This method displays a form for paying bills using a credit card.
	  *
	  * @param model  The model to be used for rendering the view.
	  * @param session The HTTP session containing the logged-in user information.
	  * @return The name of the view to be rendered.
	  */
	@GetMapping("/creditCard/paybills")
	public String paybills(Model model, HttpSession session) {
		if (session != null && session.getAttribute("loggedUser") != null) {
			// Get logged user
			User currentUser = (User) session.getAttribute("loggedUser");
			List<CreditCard> ccList = currentUser.getCreditCards();

			List<Account> AccountList = accountService.findAllAccountsByUserId(currentUser.getUserId());
			
			
			
			// add user and account list to the model
			model.addAttribute("AccountList", AccountList);
			model.addAttribute("user", currentUser);
			model.addAttribute("CcList", ccList);

			return "pay-bills";
		} else {
			model.addAttribute("error", true);
			logger.warn("User Is not logged-in. Please login first");
			return "login";
		}
	}
	
	 /**
     * This method handles the submission of a bill payment form.
     *
     * @param model          The model to be used for rendering the view.
     * @param session        The HTTP session containing the logged-in user information.
     * @param creditCardId   The ID of the credit card being used for payment.
     * @param paymentAmount  The amount of the payment.
     * @param balanceType    The type of balance being paid (custom, minimum, statement, current).
     * @param redirectAttributes The redirect attributes to be used for passing data to the redirect.
     * @return The name of the view to be rendered.
     */
	@PostMapping("/creditCard/paybills")
	public String makeCcbills(Model model, HttpSession session, @RequestParam("creditCardId") Long creditCardId,
			@RequestParam(name = "payment", required = false) Double paymentAmount,
			@RequestParam("balanceType") String balanceType,
			@RequestParam("accountId") long accountId,
			RedirectAttributes redirectAttributes) {
		
		
		
		
		if (accountId == 0) {
	        redirectAttributes.addAttribute("NotChooseAccountError", "true");
	        return "redirect:/creditCard/paybills";
	    }else if(creditCardId == 0) {
	        redirectAttributes.addAttribute("NotChooseCreditCardError", "true");
	        return "redirect:/creditCard/paybills";
	    }
		
		// Get logged user
		User currentUser = (User) session.getAttribute("loggedUser");
		Account account = accountService.findById(accountId);
		CreditCard creditCard = creditCardService.findById(creditCardId);
		MerchantCategoryCode mccBill = merchantCategoryCodeService.findByMerchantCategory("Bill");
		Transaction transaction = null;

		ForeignExchangeCurrency currency = currencyService.getCurrencyByCode("SGD");
		
		
		
		
		
		if (balanceType.equals("custom")) {
			transaction = new Transaction("CC Bill Payment", paymentAmount, null, 0.00, creditCard, account, mccBill,
					currency);
		} else if (balanceType.equals("minimum")) {
			if (creditCard.getMonthlyBalance() < 50)
				transaction = new Transaction("CC Bill Payment", creditCard.getMonthlyBalance(), null, 0.00, creditCard,
						account, mccBill, currency);

			else
				transaction = new Transaction("CC Bill Payment", 50, null, 0.00, creditCard, account, mccBill, currency);
		} else if (balanceType.equals("statement")) {
			transaction = new Transaction("CC Bill Payment", creditCard.getMonthlyBalance(), null, 0.00, creditCard,

					account, mccBill, currency);
		} else if (balanceType.equals("current")) {
			transaction = new Transaction("CC Bill Payment", creditCard.getAmountUsed(), null, 0.00, creditCard, account,
					mccBill, currency);
		}
		account.setBalance(account.getBalance()-transaction.getTransactionAmount());
		accountService.update(account);
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
