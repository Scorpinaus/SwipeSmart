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
        String viewName = creditCardController.makeCcbills(model, session, 1L, null, "custom", 0L, redirectAttributes);

        // Assert
        assertEquals("redirect:/creditCard/paybills", viewName);
        verify(redirectAttributes).addAttribute("NotChooseAccountError", "true");
        verifyNoMoreInteractions(accountService, creditCardService, mccService, currencyService,
                transactionService, userService);
	}
	
	@Test
	@DisplayName("Test for post request to make payment when Credit Card is not chosen")
	void test11() {
		// Arrange
		when(redirectAttributes.addAttribute(any(String.class), any(String.class))).thenReturn(redirectAttributes);
		
        // Act
        String viewName = creditCardController.makeCcbills(model, session, 0L, null, "custom", 1L, redirectAttributes);

        // Assert
        assertEquals("redirect:/creditCard/paybills", viewName);
        verify(redirectAttributes).addAttribute("NotChooseCreditCardError", "true");
        verifyNoMoreInteractions(accountService, creditCardService, mccService, currencyService,
                transactionService, userService);
	}
	
	@Test
	@DisplayName("Test for post request to make custom payment")
	void test12() {
		// Arrange
	    Long creditCardId = 1L;
	    Double paymentAmount = 500.0;
	    String balanceType = "custom";
	    Long accountId = 2L;
	    User currentUser = new User();
	    CreditCard creditCard = new CreditCard();
	    Account account = new Account();
	    creditCard.setCreditCardId(creditCardId);
	    account.setAccountId(accountId);
	    when(session.getAttribute("loggedUser")).thenReturn(currentUser);
	    when(creditCardService.findById(creditCardId)).thenReturn(creditCard);
	    when(accountService.findById(accountId)).thenReturn(account);
	    when(currencyService.getCurrencyByCode("SGD")).thenReturn(new ForeignExchangeCurrency());

	    // Act
	    String viewName = creditCardController.makeCcbills(model, session, creditCardId, paymentAmount, balanceType, accountId, redirectAttributes);

	    // Assert
	    assertEquals("redirect:/userCards", viewName);
	    verify(accountService).findById(account.getAccountId());
	    verify(creditCardService).findById(creditCard.getCreditCardId());
	    verify(mccService).findByMerchantCategory("Bill");
	    verify(currencyService).getCurrencyByCode("SGD");
	    verify(transactionService).persist(any(Transaction.class));
	    verify(transactionService).updateCreditCardBalance(any(Transaction.class));
	    verify(accountService).update(account);
	    verify(userService).update(currentUser);
	    verify(session).setAttribute("loggedUser", currentUser);
	    verifyNoMoreInteractions(accountService, creditCardService, mccService, transactionService, userService);
	}
	
	@Test
	@DisplayName("Test for post request to make minimum payment")
	void test13() {
		// Arrange
	    Long creditCardId = 1L;
	    Double paymentAmount = 500.0;
	    String balanceType = "minimum";
	    Long accountId = 2L;
	    User currentUser = new User();
	    CreditCard creditCard = new CreditCard();
	    Account account = new Account();
	    creditCard.setCreditCardId(creditCardId);
	    account.setAccountId(accountId);
	    when(session.getAttribute("loggedUser")).thenReturn(currentUser);
	    when(creditCardService.findById(creditCardId)).thenReturn(creditCard);
	    when(accountService.findById(accountId)).thenReturn(account);
	    when(currencyService.getCurrencyByCode("SGD")).thenReturn(new ForeignExchangeCurrency());

	    // Act
	    String viewName = creditCardController.makeCcbills(model, session, creditCardId, paymentAmount, balanceType, accountId, redirectAttributes);

	    // Assert
	    assertEquals("redirect:/userCards", viewName);
	    verify(accountService).findById(account.getAccountId());
	    verify(creditCardService).findById(creditCard.getCreditCardId());
	    verify(mccService).findByMerchantCategory("Bill");
	    verify(currencyService).getCurrencyByCode("SGD");
	    verify(transactionService).persist(any(Transaction.class));
	    verify(transactionService).updateCreditCardBalance(any(Transaction.class));
	    verify(accountService).update(account);
	    verify(userService).update(currentUser);
	    verify(session).setAttribute("loggedUser", currentUser);
	    verifyNoMoreInteractions(accountService, creditCardService, mccService, transactionService, userService);
	}
	
	@Test
	@DisplayName("Test for post request to make statement payment")
	void test14() {
		// Arrange
	    Long creditCardId = 1L;
	    Double paymentAmount = 500.0;
	    String balanceType = "statement";
	    Long accountId = 2L;
	    User currentUser = new User();
	    CreditCard creditCard = new CreditCard();
	    Account account = new Account();
	    creditCard.setCreditCardId(creditCardId);
	    account.setAccountId(accountId);
	    when(session.getAttribute("loggedUser")).thenReturn(currentUser);
	    when(creditCardService.findById(creditCardId)).thenReturn(creditCard);
	    when(accountService.findById(accountId)).thenReturn(account);
	    when(currencyService.getCurrencyByCode("SGD")).thenReturn(new ForeignExchangeCurrency());

	    // Act
	    String viewName = creditCardController.makeCcbills(model, session, creditCardId, paymentAmount, balanceType, accountId, redirectAttributes);

	    // Assert
	    assertEquals("redirect:/userCards", viewName);
	    verify(accountService).findById(account.getAccountId());
	    verify(creditCardService).findById(creditCard.getCreditCardId());
	    verify(mccService).findByMerchantCategory("Bill");
	    verify(currencyService).getCurrencyByCode("SGD");
	    verify(transactionService).persist(any(Transaction.class));
	    verify(transactionService).updateCreditCardBalance(any(Transaction.class));
	    verify(accountService).update(account);
	    verify(userService).update(currentUser);
	    verify(session).setAttribute("loggedUser", currentUser);
	    verifyNoMoreInteractions(accountService, creditCardService, mccService, transactionService, userService);
	}
	
	@Test
	@DisplayName("Test for post request to make current payment")
	void test15() {
		// Arrange
	    Long creditCardId = 1L;
	    Double paymentAmount = 500.0;
	    String balanceType = "current";
	    Long accountId = 2L;
	    User currentUser = new User();
	    CreditCard creditCard = new CreditCard();
	    Account account = new Account();
	    creditCard.setCreditCardId(creditCardId);
	    account.setAccountId(accountId);
	    when(session.getAttribute("loggedUser")).thenReturn(currentUser);
	    when(creditCardService.findById(creditCardId)).thenReturn(creditCard);
	    when(accountService.findById(accountId)).thenReturn(account);
	    when(currencyService.getCurrencyByCode("SGD")).thenReturn(new ForeignExchangeCurrency());

	    // Act
	    String viewName = creditCardController.makeCcbills(model, session, creditCardId, paymentAmount, balanceType, accountId, redirectAttributes);

	    // Assert
	    assertEquals("redirect:/userCards", viewName);
	    verify(accountService).findById(account.getAccountId());
	    verify(creditCardService).findById(creditCard.getCreditCardId());
	    verify(mccService).findByMerchantCategory("Bill");
	    verify(currencyService).getCurrencyByCode("SGD");
	    verify(transactionService).persist(any(Transaction.class));
	    verify(transactionService).updateCreditCardBalance(any(Transaction.class));
	    verify(accountService).update(account);
	    verify(userService).update(currentUser);
	    verify(session).setAttribute("loggedUser", currentUser);
	    verifyNoMoreInteractions(accountService, creditCardService, mccService, transactionService, userService);
	}
}
