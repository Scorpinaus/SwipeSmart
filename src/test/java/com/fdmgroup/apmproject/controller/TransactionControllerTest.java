package com.fdmgroup.apmproject.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.service.AccountService;
import com.fdmgroup.apmproject.service.CreditCardService;
import com.fdmgroup.apmproject.service.TransactionService;
import com.fdmgroup.apmproject.service.UserService;

import jakarta.servlet.http.HttpSession;

@SpringBootTest
public class TransactionControllerTest {
	
	@Autowired
	private UserService userService;
	@Autowired
	private CreditCardService creditCardService;
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
    void testViewCardTransactions_AccountTransactions() {
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
        verify(model).addAttribute("account", mockAccount);
        verify(model).addAttribute("transactions", mockTransactions);
    }
	
	
}
