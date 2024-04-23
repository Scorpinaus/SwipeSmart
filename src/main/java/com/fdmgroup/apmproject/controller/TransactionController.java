package com.fdmgroup.apmproject.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.ForeignExchangeCurrency;
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.service.AccountService;
import com.fdmgroup.apmproject.service.CreditCardService;
import com.fdmgroup.apmproject.service.TransactionService;
import com.fdmgroup.apmproject.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * This class is a Spring MVC controller responsible for handling requests related to transactions.
 * It provides methods for viewing transactions for credit cards and accounts.
 *
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */
@Controller
public class TransactionController {

	@Autowired
	private UserService userService;
	@Autowired
	private CreditCardService creditCardService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private TransactionService transactionService;

	private static Logger logger = LogManager.getLogger(CreditCardController.class);

	private List<ForeignExchangeCurrency> currencies;
	
	
	 /**
     * This method displays a list of transactions for a given credit card or account.
     *
     * @param month           The month for which to filter transactions (optional).
     * @param creditCardId    The ID of the credit card to filter transactions for (optional).
     * @param accountId        The ID of the account to filter transactions for (optional).
     * @param model           The model to be used for rendering the view.
     * @param session         The HTTP session containing the logged-in user information.
     * @return The name of the view to be rendered.
     */
	@PostMapping("/viewTransactions")
	public String viewCardTransactions(
			@RequestParam(name = "month", required = false) String month,
			@RequestParam(name = "creditCardId", required = false) String creditCardId,
			@RequestParam(name = "accountId", required = false) String accountId, Model model, HttpSession session) {

		if (!(session != null && session.getAttribute("loggedUser") != null)) {
			model.addAttribute("error", true);
			logger.warn("User Is not logged-in. Please login first");
			return "userCards";
		} else {
			// Retrieves current logged on user and adds as a model attribute for front-end processing.
			User loggedUser = (User) session.getAttribute("loggedUser");
			model.addAttribute("user", loggedUser);
			List<Transaction> transactions = new ArrayList<>();
			// Checks if accountID is avaliable
			if (accountId != null) {
				// Retrieves userAccount based on present account ID
				Account userAccount = accountService.findById(Long.parseLong(accountId));
				
				// Retrieves and sorts all bank account transactions based on userAccount only.
				if (month == null || month == "") {
					transactions = userAccount.getTransactions();
					Collections.sort(transactions, Comparator.comparing(Transaction::getTransactionDate));

				} else {
					//Gets and sorts bank-account transactions based on both date and userAccount
					int year = Integer.parseInt(month.substring(0, 4));
					int monthValue = Integer.parseInt(month.substring(5));
					transactions = transactionService.getTransactionsByMonthAndYearAndTransactionAccount(year,
							monthValue, userAccount);
					Collections.sort(transactions, Comparator.comparing(Transaction::getTransactionDate));
				}
				
				//Adds modelAttribute for front-end viewing
				model.addAttribute("transactions", transactions);
				model.addAttribute("account", userAccount);

			} else if (creditCardId != null) {
				//Retrieves user selected credit card entity
				CreditCard userCreditCard = creditCardService.findById(Long.parseLong(creditCardId));
				if (month == null || month == "") {
					transactions = transactionService.findTransactionsBeforeDateAndCreditCard(LocalDateTime.now(), userCreditCard);
					Collections.sort(transactions, Comparator.comparing(Transaction::getTransactionDate));
				} else {
					// Return and sort transactions for selected credit card by transaction date
					int year = Integer.parseInt(month.substring(0, 4));
					int monthValue = Integer.parseInt(month.substring(5));
					transactions = transactionService.getTransactionsByMonthAndYearAndTransactionCreditCard(year,
							monthValue, userCreditCard);
					Collections.sort(transactions, Comparator.comparing(Transaction::getTransactionDate));
				}
				model.addAttribute("creditCard", userCreditCard);
				model.addAttribute("transactions", transactions);
			}
			return "view-transactions";

		}
	}
	
	@PostMapping("/convertToInstallments")
	public String convertToInstallments(@RequestParam("transactionId") String transactionId,
	                                    @RequestParam(name = "creditCardId", required = false) String creditCardId,
	                                    Model model, HttpSession session) {
		User loggedUser = (User) session.getAttribute("loggedUser");
		// Retrieves credit card and selected transaction to convert to installments.
		CreditCard creditCard = creditCardService.findById(Long.parseLong(creditCardId));
		Transaction selectedTransaction = transactionService.findById(Long.parseLong(transactionId));
		
		// Determines new installment dates
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime oneMonthLater = currentDateTime.plusMonths(1);
        LocalDateTime twoMonthsLater = currentDateTime.plusMonths(2);
        Transaction transaction1 = new Transaction(currentDateTime, "CC Purchase", selectedTransaction.getTransactionAmount()/3, null,
				selectedTransaction.getCashback()/3, creditCard, null, selectedTransaction.getTransactionMerchantCategoryCode(), selectedTransaction.getTransactionCurrency());
        Transaction transaction2 = new Transaction(oneMonthLater, "CC Purchase", selectedTransaction.getTransactionAmount()/3, null,
        		selectedTransaction.getCashback()/3, creditCard, null, selectedTransaction.getTransactionMerchantCategoryCode(), selectedTransaction.getTransactionCurrency());
        Transaction transaction3 = new Transaction(twoMonthsLater, "CC Purchase", selectedTransaction.getTransactionAmount()/3, null,
        		selectedTransaction.getCashback()/3, creditCard, null, selectedTransaction.getTransactionMerchantCategoryCode(), selectedTransaction.getTransactionCurrency());
	    transaction1.setDescription("1st month Installment, " +  selectedTransaction.getDescription());
	    transaction2.setDescription("2nd month Installment, " +  selectedTransaction.getDescription());
	    transaction3.setDescription("3rd month Installment, " +  selectedTransaction.getDescription());
	    
	    // Redirect the user back to the page displaying the transactions and updates transaction to database.
	    transactionService.persist(transaction1);
	    transactionService.persist(transaction2);
	    transactionService.persist(transaction3);
	    transactionService.deleteById(selectedTransaction.getTransactionId());
	    creditCard.setTransactions(transactionService.findTransactionsByCreditCard(creditCard));
	    creditCardService.update(creditCard);
	    List<CreditCard> userCreditCards = loggedUser.getCreditCards();
		List<CreditCard> newUserCreditCards = new ArrayList<>();
		for (CreditCard c : userCreditCards) {
			if (c.getCreditCardId() != creditCard.getCreditCardId()) {
				newUserCreditCards.add(c);
			}
			
		}
		
		// Replaces current creditcard entity with updated credit card entity, updates user and their avaliable credit card list. Sorts before redirecting user back to credit card dashboard page.
		newUserCreditCards.add(creditCard);
		Collections.sort(newUserCreditCards, Comparator.comparing(CreditCard::getCreditCardId));
		loggedUser.setCreditCardList(newUserCreditCards);
		userService.update(loggedUser);
		session.setAttribute("loggedUser", loggedUser);
	   
	    return "redirect:/userCards";

	}
}
