package com.fdmgroup.apmproject.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.ForeignExchangeCurrency;
import com.fdmgroup.apmproject.model.Status;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.service.AccountService;
import com.fdmgroup.apmproject.service.CreditCardService;
import com.fdmgroup.apmproject.service.ForeignExchangeCurrencyService;
import com.fdmgroup.apmproject.service.MerchantCategoryCodeService;
import com.fdmgroup.apmproject.service.StatusService;
import com.fdmgroup.apmproject.service.TransactionService;
import com.fdmgroup.apmproject.service.UserService;

import jakarta.servlet.http.HttpSession;

@SpringBootTest
class CreditCardControllerTest {
	
	@InjectMocks
	private CreditCardController creditCardController;
	
	@Mock
	private CreditCardService creditCardService;
	
	@Mock
	private ForeignExchangeCurrencyService currencyService;

    @Mock
    private StatusService statusService;

    @Mock
    private UserService userService;
	
    @Mock
    private AccountService accountService;
    
    @Mock
    private MerchantCategoryCodeService mccService;
    
    @Mock
    private TransactionService transactionService;
    
    @Mock
    private RedirectAttributes redirectAttributes;
    
	@Mock
    HttpSession session;

    @Mock
    Model model;

	@BeforeEach
	void setUp() {
		creditCardController = new CreditCardController(creditCardService, currencyService, statusService, userService, accountService, mccService, transactionService);
		reset(session, model);
	}
	
	@Test
	@DisplayName("Test for get request to credit card dashboard when User is logged in")
	void test1() {
		// Arrange
        User loggedUser = new User();
        List<CreditCard> userCreditCards = new ArrayList<>();
        loggedUser.setCreditCardList(userCreditCards);
        when(session.getAttribute("loggedUser")).thenReturn(loggedUser);

        // Act
        String viewName = creditCardController.viewCreditCards(model, session);

        // Assert
        assertEquals("card-dashboard", viewName);
        verify(model).addAttribute("cards", userCreditCards);
        verify(model).addAttribute("user", loggedUser);
        verify(session, times(2)).getAttribute("loggedUser");
	}
	
	@Test
	@DisplayName("Test for get request to credit card dashboard when User is not logged in")
	void test2() {
		 // Arrange
        when(session.getAttribute("loggedUser")).thenReturn(null);

        // Act
        String viewName = creditCardController.viewCreditCards(model, session);

        // Assert
        assertEquals("card-dashboard", viewName);
        verify(model).addAttribute("error", true);
        verify(session).getAttribute("loggedUser");
	}
	
	@Test
	@DisplayName("Test for get request to apply for credit cards")
	void test3() {
		// Arrange
        User loggedUser = new User(); // You need to have User class and its instantiation here
        when(session.getAttribute("loggedUser")).thenReturn(loggedUser);

        // Act
        String viewName = creditCardController.applyCreditCard(model, session);

        // Assert
        assertEquals("apply-credit-card", viewName);
        verify(model).addAttribute("user", loggedUser);
        verify(session, times(2)).getAttribute("loggedUser");
	}
	
	@Test
	@DisplayName("Test for post request to apply for credit cards when monthly salary is blank")
	void test4() {
		// Arrange
        String monthlySalary = "";
        String cardType = "test";
        when(session.getAttribute("loggedUser")).thenReturn(new User());

        // Act
        String viewName = creditCardController.applyCreditCard(model, session, monthlySalary, cardType);

        // Assert
        assertEquals("apply-credit-card", viewName);
        verify(model).addAttribute("error", true);
        verify(session).getAttribute("loggedUser");
        verifyNoMoreInteractions(creditCardService, currencyService, statusService, userService);
	}
	
	@Test
	@DisplayName("Test for post request to apply for credit cards when card type is blank")
	void test5() {
		// Arrange
        String monthlySalary = "10000";
        String cardType = "";
        when(session.getAttribute("loggedUser")).thenReturn(new User());

        // Act
        String viewName = creditCardController.applyCreditCard(model, session, monthlySalary, cardType);

        // Assert
        assertEquals("apply-credit-card", viewName);
        verify(model).addAttribute("error", true);
        verify(session).getAttribute("loggedUser");
        verifyNoMoreInteractions(creditCardService, currencyService, statusService, userService);
	}
	
	@Test
	@DisplayName("Test for post request to apply for credit cards when monthly salary is below $1000")
	void test6() {
		// Arrange
        String monthlySalary = "100";
        String cardType = "test";
        when(session.getAttribute("loggedUser")).thenReturn(new User());

        // Act
        String viewName = creditCardController.applyCreditCard(model, session, monthlySalary, cardType);

        // Assert
        assertEquals("apply-credit-card", viewName);
        verify(model).addAttribute("error2", true);
        verify(session).getAttribute("loggedUser");
        verifyNoMoreInteractions(creditCardService, currencyService, statusService, userService);
	}
	
	@Test
	@DisplayName("Test for post request to apply for credit cards successfully")
	void test7() {
		// Arrange
        String monthlySalary = "3000"; // Assuming a sufficient salary
        String cardType = "Platinum"; // Just an example card type
        User loggedUser = new User();
        when(session.getAttribute("loggedUser")).thenReturn(loggedUser);
        ForeignExchangeCurrency sgd = new ForeignExchangeCurrency();
        sgd.setCode("SGD");
        when(currencyService.getCurrencyByCode("SGD")).thenReturn(sgd);
        Status pending = new Status();
        pending.setStatusName("Pending");
        when(statusService.findByStatusName("Pending")).thenReturn(new Status());

        // Act
        String viewName = creditCardController.applyCreditCard(model, session, monthlySalary, cardType);

        // Assert
        assertEquals("redirect:/userCards", viewName);
        verify(creditCardService).persist(any(CreditCard.class));
        verify(userService).update(loggedUser);
        verify(session).getAttribute("loggedUser");
        verify(currencyService).getCurrencyByCode("SGD");
        verify(statusService).findByStatusName("Pending");
	}
	
	@Test
	@DisplayName("Test for get request to view payment webpage when User is logged in")
	void test8() {
		// Arrange
        User currentUser = new User();
        List<CreditCard> ccList = new ArrayList<>();
        List<Account> accountList = new ArrayList<>();
        currentUser.setCreditCardList(ccList);
        when(session.getAttribute("loggedUser")).thenReturn(currentUser);
        when(accountService.findAllAccountsByUserId(currentUser.getUserId())).thenReturn(accountList);

        // Act
        String viewName = creditCardController.paybills(model, session);

        // Assert
        assertEquals("pay-bills", viewName);
        verify(model).addAttribute("AccountList", accountList);
        verify(model).addAttribute("user", currentUser);
        verify(model).addAttribute("CcList", ccList);
        verify(session, times(2)).getAttribute("loggedUser");
        verify(accountService).findAllAccountsByUserId(currentUser.getUserId());
	}
	
	@Test
	@DisplayName("Test for get request to view payment webpage when User is not logged in")
	void test9() {
		// Arrange
        when(session.getAttribute("loggedUser")).thenReturn(null);

        // Act
        String viewName = creditCardController.paybills(model, session);

        // Assert
        assertEquals("login", viewName);
        verify(model).addAttribute("error", true);
        verify(session).getAttribute("loggedUser");
        verifyNoMoreInteractions(accountService);
	}
	
	@Test
	@DisplayName("Test for post request to make payment when Account is not chosen")
	void test10() {
		// Arrange
		when(redirectAttributes.addAttribute(any(String.class), any(String.class))).thenReturn(redirectAttributes);
		
        // Act
        String viewName = creditCardController.makeCcbills(model, session, 0L, null, "custom", 0L, redirectAttributes);

        // Assert
        assertEquals("redirect:/creditCard/paybills", viewName);
        verify(redirectAttributes).addAttribute("NotChooseAccountError", "true");
        verifyNoMoreInteractions(accountService, creditCardService, mccService, currencyService,
                transactionService, userService);
	}
	
	@Test
	@DisplayName("")
	void test11() {
		
	}
	
	@Test
	@DisplayName("")
	void test12() {
		
	}
	
	@Test
	@DisplayName("")
	void test13() {
		
	}
	
	@Test
	@DisplayName("")
	void test14() {
		
	}
	
	@Test
	@DisplayName("")
	void test15() {
		
	}
}
