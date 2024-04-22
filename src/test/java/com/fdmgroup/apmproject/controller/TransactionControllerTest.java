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
	
    @Test
    void testViewCardTransactions_UserNotLoggedIn() {
        // Arrange
        when(session.getAttribute("loggedUser")).thenReturn(null);
        
        // Act
        String viewName = transactionController.viewCardTransactions(null, null, null, model, session);
        
        // Assert
        assertEquals("userCards", viewName);
        verify(model).addAttribute("error", true);
    }
	
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
