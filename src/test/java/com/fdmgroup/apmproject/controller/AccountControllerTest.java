package com.fdmgroup.apmproject.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.fdmgroup.apmproject.model.Status;
import com.fdmgroup.apmproject.model.Transaction;
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
	
	@Test
	@DisplayName("Test createBankAccount function for successful account creation")
	void testCreateBankAccountOne() {
		//Arrange
		String accountName = "Savings Account";
		double initialDeposit = 6000.0;
		User currentUser = user;
		session.setAttribute("loggerUser", currentUser);
		Account accountOne = account;
		accountOne.setAccountName(accountName);
		accountOne.setBalance(initialDeposit);
		accountOne.setAccountNumber("1234567890");
		accountOne.setAccountUser(currentUser);
		accountOne.setAccountStatus(new Status("Pending"));
		
		when(currencyService.getCurrencyByCode("SGD")).thenReturn(foreignCurrency);
		when(statusService.findByStatusName("Pending")).thenReturn(new Status("Pending"));
		when(accountService.generateUniqueAccountNumber()).thenReturn("1234567890");
		
		//Act
		String viewName = accountController.createBankAccount(accountName, initialDeposit, session, redirectAttributes);
		
		//Assert
		assertEquals("redirect:/bankaccount/dashboard", viewName);
		verify(accountService).persist(accountOne);
		verify(transactionService).persist(any(Transaction.class));
	}
	
	@Test
	@DisplayName("Test createBankAccount function for insufficient initial deposit")
	void testCreateBankAccountTwo() {
		//Arrange
		String accountName = "Savings Account";
		double initialDeposit = 50.0;
		
		//Act
		String viewName = accountController.createBankAccount(accountName, initialDeposit, session, redirectAttributes);
		
		//Assert
		assertEquals("redirect:/bankaccount/create", viewName);
		verify(redirectAttributes).addAttribute("InsufficientInitialDepositError", "true");
		verifyNoInteractions(accountService);
	}
	
	@Test
	@DisplayName("Test createBankAccount function for Empty or blank account name")
	void testCreateBankAccountThree() {
		//Arrange
		String accountName = " ";
		double initialDeposit = 150.0;
		
		//Act
		String viewName = accountController.createBankAccount(accountName, initialDeposit, session, redirectAttributes);
		
		//Assert
		assertEquals("redirect:/bankaccount/create", viewName);
		verifyNoInteractions(accountService);
	}
	
	@Test
	@DisplayName("Test to go for transfer page for successful page load")
	void testGoToTransferPageOne() {
		//Arrange
		User currentUser = user;
		currentUser.setUserId(1L);
		session.setAttribute("loggedUser", currentUser);
		Account accountOne = account;
		Account accountTwo = account;
		List<Account> accountList = new ArrayList<>();
		accountList.add(accountOne);
		accountList.add(accountTwo);
		ForeignExchangeCurrency currencyUSD = foreignCurrency;
		currencyUSD.setCode("USD");
		ForeignExchangeCurrency currencyEUR = foreignCurrency;
		currencyUSD.setCode("EUR");
		List<ForeignExchangeCurrency> currencyList = new ArrayList<>();
		currencyList.add(currencyUSD);
		currencyList.add(currencyEUR);
		
		when(accountService.findAllAccountsByUserId(currentUser.getUserId())).thenReturn(accountList);
       when(currencyService.getSupportedCurrencies()).thenReturn(currencyList);
        
        //Act
        String viewName = accountController.goToTransferPage(model, session);
        
        //Assert
       assertEquals("transfer", viewName);
       verify(model).addAttribute("AccountList", accountList);		
	}
	
	@Test
	@DisplayName("Test bankAccount transfer function to same account")
	void testBankAccountTransferOne() {
		//Arrange
		String accountNumber = "111111111";
		Account accountOne = account;
		accountOne.setAccountNumber(accountNumber);
		accountOne.setAccountId(1L);
		accountOne.setBalance(100.0);
		accountOne.setCurrencyCode("USD");
		when(accountService.findById(accountOne.getAccountId())).thenReturn(accountOne);
		when(currencyService.getExchangeRate("USD", "USD")).thenReturn(BigDecimal.ONE);
		
		
		//Act
		String result = accountController.transferMoney(accountOne.getAccountId(), 100.0, accountNumber, "USD", session, redirectAttributes);
		
		//Assert
		assertEquals("redirect:/bankaccount/transfer", result);
		verify(redirectAttributes).addAttribute("SameAccountError", "true");
	}
	
	@Test
	@DisplayName("Test bankAccount transfer function for insufficient funds")
	void testBankAccountTransferTwo() {
		//Arrange
		String accountNumber = "111111111";
		Account accountOne = account;
		accountOne.setAccountNumber(accountNumber);
		accountOne.setAccountId(1L);
		accountOne.setBalance(100.0);
		accountOne.setCurrencyCode("USD");
		when(accountService.findById(accountOne.getAccountId())).thenReturn(accountOne);
		when(currencyService.getExchangeRate("USD", "USD")).thenReturn(BigDecimal.ONE);
		
		//Act
		String result = accountController.transferMoney(1L, 1000.0, "987654321", "USD", session, redirectAttributes);

		
		//Assert
		assertEquals("redirect:/bankaccount/transfer", result);
		verify(redirectAttributes).addAttribute("InsufficientBalanceError", "true");
		
	}
	
	@Test
	@DisplayName("Test bankAccount Transfer for successful internal transfer")
	void testBankAccountTransferThree() {
		//Arrange
		Account accountOne = new Account();
        accountOne.setAccountNumber("111111111");
        accountOne.setAccountId(1L);
        accountOne.setBalance(1000.0);
        accountOne.setCurrencyCode("USD");
				
        Account accountTwo = new Account();
        accountTwo.setAccountNumber("999999999");
        accountTwo.setAccountId(2L);
        accountTwo.setBalance(1000.0);
        accountTwo.setCurrencyCode("USD");
        accountTwo.setAccountStatus(new Status("Active"));
				        when(accountService.findById(accountOne.getAccountId())).thenReturn(accountOne);
        when(currencyService.getExchangeRate("USD", "USD")).thenReturn(BigDecimal.ONE);
        when(accountService.findAccountByAccountNumber(accountTwo.getAccountNumber())).thenReturn(accountTwo);
        double transferAmount = 100.0;
				
				//Act
				String result = accountController.transferMoney(accountOne.getAccountId(), transferAmount, accountTwo.getAccountNumber(), "USD", session, redirectAttributes);

				
				//Assert
				assertEquals("redirect:/bankaccount/dashboard", result);
	}
	
	@Test
	@DisplayName("Test bankAccount transfer to pending Account")
	void testBankAccountTransferFour() {
	//Arrange
		Account accountOne = new Account();
        accountOne.setAccountNumber("111111111");
        accountOne.setAccountId(1L);
        accountOne.setBalance(1000.0);
        accountOne.setCurrencyCode("USD");
        accountOne.setAccountStatus(new Status("Active"));
		Status pendingStatus = new Status("Pending");
        Account accountTwo = new Account();
        accountTwo.setAccountNumber("999999999");
        accountTwo.setAccountId(2L);
        accountTwo.setBalance(1000.0);
        accountTwo.setCurrencyCode("USD");
        accountTwo.setAccountStatus(pendingStatus);
        
				        when(accountService.findById(accountOne.getAccountId())).thenReturn(accountOne);
        when(currencyService.getExchangeRate("USD", "USD")).thenReturn(BigDecimal.ONE);
        when(accountService.findAccountByAccountNumber(accountTwo.getAccountNumber())).thenReturn(accountTwo);
        when(statusService.findByStatusName("Pending")).thenReturn(pendingStatus);
        double transferAmount = 100.0;
	
	//Act
	String result = accountController.transferMoney(accountOne.getAccountId(), transferAmount, accountTwo.getAccountNumber(), "USD", session, redirectAttributes);
        
	//Assert
	assertEquals("redirect:/bankaccount/transfer", result);
	verify(redirectAttributes).addAttribute("RecipientAccountPendingError", "true");
	}
	
	@Test
	@DisplayName("Test bankAccount transfer for external transfer")
	void testBankAccountTransferFive() {
		//Arrange
		Account accountOne = new Account();
        accountOne.setAccountNumber("111111111");
        accountOne.setAccountId(1L);
        accountOne.setBalance(1000.0);
        accountOne.setCurrencyCode("USD");
        accountOne.setAccountStatus(new Status("Active"));
				        when(accountService.findById(accountOne.getAccountId())).thenReturn(accountOne);
        when(currencyService.getExchangeRate("USD", "USD")).thenReturn(BigDecimal.ONE);
        when(accountService.findAccountByAccountNumber("external-1")).thenReturn(null);
        double transferAmount = 100.0;
		
		//Act
        String result = accountController.transferMoney(accountOne.getAccountId(), transferAmount, "external-1", "USD", session, redirectAttributes);
        
		
		//Assert
        assertEquals("redirect:/bankaccount/dashboard", result);
        verify(transactionService).persist(any(Transaction.class));
	}
}
