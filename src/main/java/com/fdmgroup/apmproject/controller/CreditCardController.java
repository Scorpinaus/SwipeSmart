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
		// Checks if user is logged on. If not logged-in, user redirected to log-in page
		if (session != null && session.getAttribute("loggedUser") != null) {
			//Retrieves loggedUser details and list of credit card that loggedUser has. Adds these attributes and sorts them according to creditCardId
			User loggedUser = (User) session.getAttribute("loggedUser");
			List<CreditCard> userCreditCards = loggedUser.getCreditCards();
			model.addAttribute("cards", userCreditCards);
			model.addAttribute("user", loggedUser);
			Collections.sort(userCreditCards, Comparator.comparing(CreditCard::getCreditCardId));
			return "card-dashboard";
		} else {
			model.addAttribute("error", true);
			logger.warn("User Is not logged-in. Please login first");
			//Should return to login page?
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
		// Checks if user is logged on. If user not logged on, return to login page
		if (session != null && session.getAttribute("loggedUser") != null) {
			//Adds currently logged on user details as a model attribute for subsequent processing.
			User loggedUser = (User) session.getAttribute("loggedUser");
			model.addAttribute("user", loggedUser);
			return "apply-credit-card";
		} else {
			//Should return to login page?
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
		// Retrieves logged-on User and checks for empty fields. If user submitted with empty fields, user will be redirected back to the applyCreditCard page and shown respective error.
		User loggedUser = (User) session.getAttribute("loggedUser");
		model.addAttribute("user", loggedUser);
		if (monthlySalary.isBlank() || cardType.isBlank()) {
			logger.warn("There are empty fields, please fill up");
			model.addAttribute("error", true);
			return "apply-credit-card";
		} 	else {
			// Checks for users monthly salary. If monthly salary is lower than specified limit, user is not allowed to apply and shown error after redirected.
			if (Double.parseDouble(monthlySalary) < 1000) {
				logger.warn("Your salary is too low. You are required to have a monthly salary above $1000");
				model.addAttribute("error2", true);
				return "apply-credit-card";
			} else {
				//Generates data required for credit card creation
				String creditCardNumber = creditCardService.generateCreditCardNumber();
				String pin = creditCardService.generatePinNumber();
				double cardLimit = Double.parseDouble(monthlySalary) * 3;
				
				//Sets currency to be local currency, eg: SGD
				ForeignExchangeCurrency localCurrency = currencyService.getCurrencyByCode("SGD");

				// Set credit card status as pending when they apply for the card
				Status statusName = statusService.findByStatusName("Pending");
				
				//Create credit card and persist it to mySQL database
				CreditCard createCreditCard = new CreditCard(creditCardNumber, pin, cardLimit, cardType, statusName, 0,
						loggedUser, localCurrency.getCode());
				creditCardService.persist(createCreditCard);
				logger.info("Credit card of number " + creditCardNumber + " created");
				
				// Updates list of credit cards owned by current User and updates to database.
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
		// Checks if user logged on. If user not logged on, user will be redirected to log-in page
		if (session != null && session.getAttribute("loggedUser") != null) {
			
			// Get logged user, credit card and avaliable bank accounts under current logged on user
			User currentUser = (User) session.getAttribute("loggedUser");
			List<CreditCard> ccList = currentUser.getCreditCards();
			List<Account> AccountList = accountService.findAllAccountsByUserId(currentUser.getUserId());
			
			// add user and account list to the model, before directing user to pay-bills page.
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
		
		// Get logged user, their active bank account and credit cards and other details
		User currentUser = (User) session.getAttribute("loggedUser");
		Account account = accountService.findById(accountId);
		CreditCard creditCard = creditCardService.findById(creditCardId);
		MerchantCategoryCode mccBill = merchantCategoryCodeService.findByMerchantCategory("Bill");
		Transaction transaction = null;

		ForeignExchangeCurrency currency = currencyService.getCurrencyByCode("SGD");
		
		
		
		
		//Determines type of transaction made depending on the balance type. 
		if (balanceType.equals("custom")) {
			
			transaction = new Transaction("CC Payment", paymentAmount, null, 0.00, creditCard, account, mccBill,
					currency);
		} else if (balanceType.equals("minimum")) {
			//Checks if credit card has paid the minimum balance for the month. If so, user is not billed and redirected to creditcard dashboard page. If not paid, new transaction made to record minimum balance payment.
			if (creditCard.getMinBalancePaid() <= 0 ) {
				logger.info("Minimum balance is zero, no payment made") ;
				return "redirect:/userCards";
			} else {
				transaction = new Transaction("CC Payment", creditCard.getMinBalancePaid(), null, 0.00, creditCard, account, mccBill, currency);
			}
		} else if (balanceType.equals("statement")) {
			// Checks if balance type is credit card statement. If so, amount recorded in transaction will be the credit card monthly balance.
			transaction = new Transaction("CC Payment", creditCard.getMonthlyBalance(), null, 0.00, creditCard, account, mccBill, currency);
		} else if (balanceType.equals("current")) {
			//Checks if balance type is current credit card usage for the month.
			transaction = new Transaction("CC Payment", creditCard.getAmountUsed(), null, 0.00, creditCard, account,
					mccBill, currency);
		}
		// Processes new bank account balance, updates it onto database. Creates and uploads new transaction onto database.
		account.setBalance(account.getBalance()-transaction.getTransactionAmount());
		accountService.update(account);
		transactionService.persist(transaction);
		transactionService.updateCreditCardBalance(transaction);

		logger.info("Payment of" + balanceType + " balance to credit card " + creditCard.getCreditCardNumber()
				+ " completed");
		
		// Returns list of credit card current user has and creates list of new credit cards.
		List<CreditCard> userCreditCards = currentUser.getCreditCards();
		List<CreditCard> newUserCreditCards = new ArrayList<>();
		for (CreditCard c : userCreditCards) {
			if (c.getCreditCardId() != creditCardId) {
				newUserCreditCards.add(c);
			}
			
		}
		
		// Replaces current creditcard entity with updated credit card entity, updates user and their avaliable credit card list. Sorts before redirecting user back to credit card dashboard page.
		newUserCreditCards.add(creditCard);
		Collections.sort(newUserCreditCards, Comparator.comparing(CreditCard::getCreditCardId));
		currentUser.setCreditCardList(newUserCreditCards);
		userService.update(currentUser);
		session.setAttribute("loggedUser", currentUser);
		
		return "redirect:/userCards";
	}

}
