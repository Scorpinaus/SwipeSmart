package com.fdmgroup.apmproject.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.ForeignExchangeCurrency;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.service.AccountService;
import com.fdmgroup.apmproject.service.ForeignExchangeCurrencyService;
import com.fdmgroup.apmproject.service.StatusService;
import com.fdmgroup.apmproject.service.TransactionService;
import com.fdmgroup.apmproject.service.UserService;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AccountService accountService;
	
	@MockBean
	private StatusService statusService;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private ForeignExchangeCurrencyService currencyService;
	
	@MockBean
	private TransactionService transactionService;
	
	@Autowired
	private AccountController accountController;
	
	private MockHttpSession session;
	private Model model;
	private RedirectAttributes redirectAttributes;
	private User user;
	private Account account;
	private ForeignExchangeCurrency foreignCurrency;
	
	@BeforeEach
	void setUp() {
		session = new MockHttpSession();
		model = mock(Model.class);
		user = new User();
		user.setUserId(1L);
		account = new Account();
		foreignCurrency = new ForeignExchangeCurrency();
		redirectAttributes = mock(RedirectAttributes.class);
	}
	
	@Test
	@DisplayName("Test when user is logged in that has bank accounts")
	void bankAccountDashboardOne() {
		//Arrange
		User currentUser = user;
		session.setAttribute("loggedUser", currentUser);
		List<Account> accounts = new ArrayList<>();
		accounts.add(account);
		accounts.add(account);
		when(accountService.findAllAccountsByUserId(currentUser.getUserId())).thenReturn(accounts);
		
		//Act
		String viewName = accountController.showBankAccountDashboard(session, model);
		
		//Assert
		assertEquals("account-dashboard", viewName);	
	}
	
	@Test
	@DisplayName("Test when user logged in without any bank accounts")
	void bankAccountDashboardTwo() {
		//Arrange
		User currentUser = user;
		session.setAttribute("loggedUser", currentUser);
		List<Account> accounts = new ArrayList<>();
		when(accountService.findAllAccountsByUserId(currentUser.getUserId())).thenReturn(accounts);
		
		//Act
		String viewName = accountController.showBankAccountDashboard(session, model);
		
		//Assert
		assertEquals("account-dashboard", viewName);
	}
	
	@Test
	@DisplayName("Test when user is not logged in")
	void bankAccountDashboardThree() {
		//Arrange
		
		//Act
		String viewName = accountController.showBankAccountDashboard(session, model);
		
		//Assert
		assertEquals("redirect:/login", viewName);
	}
	
	@Test
	@DisplayName("Test withdrawalBankAccount function when user is logged in with accounts")
	void withdrawalBankAccountOne() {
		//Arrange
		User currentUser = user;
		session.setAttribute("loggedUser", currentUser);
		List<Account> accounts = new ArrayList<>();
		accounts.add(account);
		List<ForeignExchangeCurrency> currencyList = new ArrayList<>();
		ForeignExchangeCurrency currencyUSD = foreignCurrency;
		currencyUSD.setCode("USD");
		ForeignExchangeCurrency currencyEUR = foreignCurrency;
		currencyUSD.setCode("EUR");
		currencyList.add(currencyUSD);
		currencyList.add(currencyEUR);
		
		when(accountService.findAllAccountsByUserId(currentUser.getUserId())).thenReturn(accounts);
		when(currencyService.getSupportedCurrencies()).thenReturn(currencyList);
		
		//Act
		String viewName = accountController.withdrawalBankAccount(session, model, redirectAttributes);
		
		//Assert
		assertEquals("withdrawal", viewName);
	}
	
	@Test
	@DisplayName("Test withdrawalBankAccount function when user is logged in with no accounts")
	void withdrawalBankAccountTwo() {
		//Arrange
		User currentUser = user;
		session.setAttribute("loggedUser", currentUser);
		List<Account> accounts = new ArrayList<>();
		List<ForeignExchangeCurrency> currencyList = new ArrayList<>();
		ForeignExchangeCurrency currencyUSD = foreignCurrency;
		currencyUSD.setCode("USD");
		ForeignExchangeCurrency currencyEUR = foreignCurrency;
		currencyUSD.setCode("EUR");
		currencyList.add(currencyUSD);
		currencyList.add(currencyEUR);
		
		when(accountService.findAllAccountsByUserId(currentUser.getUserId())).thenReturn(accounts);
		when(currencyService.getSupportedCurrencies()).thenReturn(currencyList);
		
		//Act
		String viewName = accountController.withdrawalBankAccount(session, model, redirectAttributes);
		
		//Assert
		assertEquals("account-dashboard", viewName);
	}
	
	@Test
	@DisplayName("Test withdrawalBankAccount function when user is not logged in")
	void withdrawalBankAccountThree() {
		// Arrange
	    // No user is set in the session

	    // Act
		String viewName = accountController.withdrawalBankAccount(session, model, redirectAttributes);

	    // Assert
	    assertEquals("redirect:/login", viewName);
	}
	
	@Test
	@DisplayName("Test processWithdrawal function for successful withdrawal")
	void voidWithdrawalOne() {
		//Arrange
		User currentUser = user;
		session.setAttribute("loggedUser", currentUser);
		Account accountOne = account;
		accountOne.setAccountId(1L);
		accountOne.setCurrencyCode("USD");
		accountOne.setBalance(200.00);
		ForeignExchangeCurrency currencyUSD = foreignCurrency;
		currencyUSD.setCode("USD");
		when(accountService.findById(accountOne.getAccountId())).thenReturn(accountOne);
		when(currencyService.getExchangeRate("USD", "USD")).thenReturn(BigDecimal.ONE);
		when(currencyService.getCurrencyByCode("USD")).thenReturn(currencyUSD);
		
		//Act
		String viewName = accountController.processWithdrawal(accountOne.getAccountId(), currencyUSD.getCode(), new BigDecimal("100"), session, redirectAttributes);
		
		//Assert
		assertEquals("redirect:/bankaccount/dashboard", viewName);
	}
	
	@Test
	@DisplayName("Test processWithdrawal function for non-successful withdrawal")
	void voidWithdrawalTwo() {
		//Arrange
		User currentUser = user;
		session.setAttribute("loggedUser", currentUser);
		Account accountOne = account;
		accountOne.setAccountId(1L);
		accountOne.setCurrencyCode("USD");
		accountOne.setBalance(10.00);
		when(accountService.findById(accountOne.getAccountId())).thenReturn(account);
		when(currencyService.getExchangeRate("USD", "USD")).thenReturn(BigDecimal.ONE);
		
		//Act
		String viewName = accountController.processWithdrawal(accountOne.getAccountId(), "USD", new BigDecimal("100"), session, redirectAttributes);
		
		//Assert
		assertEquals("redirect:/bankaccount/withdrawal", viewName);
		
}
	@Test
	@DisplayName("Test goDeposit page when user is logged in that has present accounts")
	void testAccessDepositOne() {
		//Arrange
		User currentUser = user;
		session.setAttribute("loggedUser", currentUser);
		Account accountOne = account;
		accountOne.setAccountId(1L);
		accountOne.setCurrencyCode("USD");
		accountOne.setBalance(10.00);

		List<Account> accounts = new ArrayList<>();
		accounts.add(accountOne);
		ForeignExchangeCurrency currencyUSD = foreignCurrency;
		currencyUSD.setCode("USD");
		ForeignExchangeCurrency currencyEUR = foreignCurrency;
		currencyUSD.setCode("EUR");
		List<ForeignExchangeCurrency> currencyList = new ArrayList<>();
		currencyList.add(currencyUSD);
		currencyList.add(currencyEUR);
		when(accountService.findAllAccountsByUserId(currentUser.getUserId())).thenReturn(accounts);
		when(currencyService.getSupportedCurrencies()).thenReturn(currencyList);
		
		//Act
		String viewName = accountController.goToDepositPage(model, session);

		//Assert
		assertEquals("deposit", viewName);
	}
	
	@Test
	@DisplayName("Test goDeposit page when user is logged in that has present accounts")
	void testAccessDepositTwo() {
	//Arrange
			User currentUser = user;
			session.setAttribute("loggedUser", currentUser);
			List<Account> accounts = new ArrayList<>();
			ForeignExchangeCurrency currencyUSD = foreignCurrency;
			currencyUSD.setCode("USD");
			ForeignExchangeCurrency currencyEUR = foreignCurrency;
			currencyUSD.setCode("EUR");
			List<ForeignExchangeCurrency> currencyList = new ArrayList<>();
			currencyList.add(currencyUSD);
			currencyList.add(currencyEUR);
			when(accountService.findAllAccountsByUserId(currentUser.getUserId())).thenReturn(accounts);
			when(currencyService.getSupportedCurrencies()).thenReturn(currencyList);
			
			//Act
			String viewName = accountController.goToDepositPage(model, session);

			//Assert
			assertEquals("deposit", viewName);
}
	
	@Test
	@DisplayName("Test deposit method for successful deposits")
	void testDepositOne() {
		//Arrange
		Account accountOne = account;
		accountOne.setAccountId(1L);
		accountOne.setCurrencyCode("USD");
		accountOne.setBalance(10.00);
		BigDecimal depositAmount = BigDecimal.valueOf(50.0);
		ForeignExchangeCurrency currencyUSD = foreignCurrency;
		currencyUSD.setCode("USD");
		BigDecimal exchangeRate = BigDecimal.ONE;
		
		when(accountService.findById(1)).thenReturn(accountOne);
		when(currencyService.getCurrencyByCode("USD")).thenReturn(currencyUSD);
		when(currencyService.getExchangeRate("USD", "USD")).thenReturn(exchangeRate);
		
		//Act
		String redirectUrl = accountController.deposit(accountOne.getAccountId(), depositAmount.doubleValue(), "USD");
		
		
		//Assert
		assertEquals("redirect:/bankaccount/dashboard", redirectUrl);
	}
	
	@Test
	@DisplayName("Test for deposit with currency conversion")
	void testDepositTwo() {
		//Arrange
		Account accountOne = account;
		accountOne.setAccountId(1L);
		accountOne.setCurrencyCode("USD");
		accountOne.setBalance(10.00);
		BigDecimal depositAmount = BigDecimal.valueOf(100.0);
		BigDecimal exchangeRate = new BigDecimal("1.2");
		BigDecimal expectedConvertedAmount = depositAmount.multiply(exchangeRate);
		ForeignExchangeCurrency currencyUSD = foreignCurrency;
		currencyUSD.setCode("USD");
		when(accountService.findById(1L)).thenReturn(accountOne);
		when(currencyService.getCurrencyByCode("USD")).thenReturn(currencyUSD);
		when(currencyService.getExchangeRate("EUR", "USD")).thenReturn(exchangeRate);
		
		//Act
		String redirectUrl = accountController.deposit(1L, depositAmount.doubleValue(), "EUR");
		
		//Arrange
		assertEquals("redirect:/bankaccount/dashboard", redirectUrl);
	}
	
	@Test
	@DisplayName("Test viewCreateBankAccountPage for logged in user")
	void testViewCreateBankAccountOne() {
		//Arrange
		User currentUser = user;
		currentUser.setUsername("jackyTan");
		session.setAttribute("loggedUser", currentUser);
		
		//Act
		String viewName = accountController.goToCreateBankAccountPage(session, model);
		
		//Assert
		assertEquals("create-bank-account", viewName);
		verify(model).addAttribute("user", currentUser);
	}
	
	@Test
	@DisplayName("Test viewCreateBankAccountPage for user not logged on")
	void testViewCreateBankAccountTwo() {
		//Arrange
		User currentUser = user;
		
		//Act
		String viewName = accountController.goToCreateBankAccountPage(session, model);
		
		//Assert
		assertEquals("create-bank-account", viewName);
	}
	
	
	
	
}
