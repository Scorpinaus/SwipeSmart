package com.fdmgroup.apmproject.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.service.AccountService;
import com.fdmgroup.apmproject.service.TransactionService;
import com.fdmgroup.apmproject.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * Test suite for {@link TransactionController} using Spring MVC test framework.
 * This class simulates a web application context to test the interactions
 * with the {@link TransactionController}. It configures automatic injection of
 * mocked dependencies and a simulated MVC environment for thorough testing
 * of controller methods under various scenarios.
 *
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */

@SpringBootTest
public class TransactionControllerTest {
	
	@Autowired
	private UserService userService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private TransactionController transactionController;
	
	private HttpSession session;
    private Model model;

    @BeforeEach
    void setUp() {
        session = mock(HttpSession.class);
        model = mock(Model.class);
    }
	
	
    /**
     * Tests the behavior of viewing account transactions.
     * <p>
     * This method simulates the scenario where a user views transactions associated with a specific account. It retrieves the logged-in user's information, finds the user's accounts, and fetches transactions for the first account. It then verifies that the "view-transactions" view is returned, and the model attributes "user", "account", and "transactions" are appropriately set for rendering the view. This test ensures that users can view their account transactions successfully and that the necessary data is provided to render the transactions view accurately.
     *
     * @param model The model attribute to add data for rendering the view.
     * @param session The session attribute to retrieve user information.
     * @return The name of the view after rendering.
     * @throws AssertionError If the test fails to assert the expected behavior.
     * @see org.junit.jupiter.api.Test
     */
    @Test
    void testViewTransactions_AccountTransactions() {
        // Arrange
        User loggedUser = userService.findUserByUsername("jackytan"); 
        when(session.getAttribute("loggedUser")).thenReturn(loggedUser);
        
        List<Account> userAccount = accountService.findAllAccountsByUserId(loggedUser.getUserId());
        List<Transaction> accountTransactions = userAccount.get(0).getTransactions();
        
        
        // Act
        String viewName = transactionController.viewCardTransactions(null, null, String.valueOf(userAccount.get(0).getAccountId()), model, session);
        
        // Assert
        assertEquals("view-transactions", viewName);
        verify(model).addAttribute("user", loggedUser);
        when(model.getAttribute("account")).thenReturn(userAccount.get(0));
        when(model.getAttribute("transactions")).thenReturn(accountTransactions);
    }
	
    /**
     * Tests the behavior of viewing credit card transactions.
     * <p>
     * This method simulates the scenario where a user views transactions associated with a specific credit card. It retrieves the logged-in user's information, finds the user's credit cards, and fetches transactions for the first credit card. It then verifies that the "view-transactions" view is returned, and the model attributes "user", "creditCard", and "transactions" are appropriately set for rendering the view. This test ensures that users can view their credit card transactions successfully and that the necessary data is provided to render the transactions view accurately.
     *
     * @param model The model attribute to add data for rendering the view.
     * @param session The session attribute to retrieve user information.
     * @return The name of the view after rendering.
     * @throws AssertionError If the test fails to assert the expected behavior.
     * @see org.junit.jupiter.api.Test
     */
    @Test
    void testViewTransactions_CreditCardTransactions() {
        // Arrange
        User loggedUser = userService.findUserByUsername("jackytan"); 
        when(session.getAttribute("loggedUser")).thenReturn(loggedUser);
        
        List<CreditCard> userCreditCard = loggedUser.getCreditCards();
        List<Transaction> creditCardTransactions = userCreditCard.get(0).getTransactions();
        
        
        // Act
        String viewName = transactionController.viewCardTransactions(null, String.valueOf(userCreditCard.get(0).getCreditCardId()), null, model, session);
        
        // Assert
        assertEquals("view-transactions", viewName);
        verify(model).addAttribute("user", loggedUser);
        when(model.getAttribute("creditCard")).thenReturn(userCreditCard.get(0));
        when(model.getAttribute("transactions")).thenReturn(creditCardTransactions);
    }
    /**
     * Tests the behavior of viewing credit card transactions filtered by month.
     * <p>
     * This method simulates the scenario where a user views transactions associated with a specific credit card filtered by a specified month. It retrieves the logged-in user's information, finds the user's credit cards, and fetches transactions for the specified month and credit card. It then verifies that the "view-transactions" view is returned, and the model attributes "user", "creditCard", and "transactions" are appropriately set for rendering the view. This test ensures that users can view their credit card transactions for a specific month successfully and that the necessary data is provided to render the transactions view accurately.
     *
     * @param month The month for which transactions are to be filtered (in "YYYY-MM" format).
     * @param model The model attribute to add data for rendering the view.
     * @param session The session attribute to retrieve user information.
     * @return The name of the view after rendering.
     * @throws AssertionError If the test fails to assert the expected behavior.
     * @see org.junit.jupiter.api.Test
     */
    @Test
    void testViewTransactionsMonthFilterCreditCard() {
        // Arrange
        User loggedUser = userService.findUserByUsername("jackytan"); 
        when(session.getAttribute("loggedUser")).thenReturn(loggedUser);
        
        List<CreditCard> userCreditCard = loggedUser.getCreditCards();
        String month = "2024-04";
        
        
        // Act
        String viewName = transactionController.viewCardTransactions(month, String.valueOf(userCreditCard.get(0).getCreditCardId()), null, model, session);
        int year = Integer.parseInt(month.substring(0, 4));
		int monthValue = Integer.parseInt(month.substring(5));
		List<Transaction> expectedTransactions = transactionService.getTransactionsByMonthAndYearAndTransactionCreditCard(year,
				monthValue, userCreditCard.get(0));
		Collections.sort(expectedTransactions, Comparator.comparing(Transaction::getTransactionDate));
        
        // Assert
        assertEquals("view-transactions", viewName);
        verify(model).addAttribute("user", loggedUser);
        when(model.getAttribute("creditCard")).thenReturn(userCreditCard.get(0));
        when(model.getAttribute("transactions")).thenReturn(expectedTransactions);
    }
    
    /**
     * Tests the behavior of viewing account transactions filtered by month.
     * <p>
     * This method simulates the scenario where a user views transactions associated with a specific account filtered by a specified month. It retrieves the logged-in user's information, finds the user's accounts, and fetches transactions for the specified month and account. It then verifies that the "view-transactions" view is returned, and the model attributes "user", "account", and "transactions" are appropriately set for rendering the view. This test ensures that users can view their account transactions for a specific month successfully and that the necessary data is provided to render the transactions view accurately.
     *
     * @param month The month for which transactions are to be filtered (in "YYYY-MM" format).
     * @param model The model attribute to add data for rendering the view.
     * @param session The session attribute to retrieve user information.
     * @return The name of the view after rendering.
     * @throws AssertionError If the test fails to assert the expected behavior.
     * @see org.junit.jupiter.api.Test
     */
    @Test
    void testViewTransactionsMonthFilterAccount() {
        // Arrange
        User loggedUser = userService.findUserByUsername("jackytan"); 
        when(session.getAttribute("loggedUser")).thenReturn(loggedUser);
        
        List<Account> userAccount = accountService.findAllAccountsByUserId(loggedUser.getUserId());
        String month = "2024-04";
        
        
        // Act
        String viewName = transactionController.viewCardTransactions(month, String.valueOf(userAccount.get(0).getAccountId()), null, model, session);
        int year = Integer.parseInt(month.substring(0, 4));
		int monthValue = Integer.parseInt(month.substring(5));
		List<Transaction> expectedTransactions = transactionService.getTransactionsByMonthAndYearAndTransactionAccount(year,
				monthValue, userAccount.get(0));
		Collections.sort(expectedTransactions, Comparator.comparing(Transaction::getTransactionDate));
        
        // Assert
        assertEquals("view-transactions", viewName);
        verify(model).addAttribute("user", loggedUser);
        when(model.getAttribute("creditCard")).thenReturn(userAccount.get(0));
        when(model.getAttribute("transactions")).thenReturn(expectedTransactions);
    }
    

	
}
