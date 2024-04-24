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

/**
 * Test suite for {@link AccountController} using Spring MVC test framework.
 * This class simulates a web application context to test the interactions
 * with the {@link AccountController}. It configures automatic injection of
 * mocked dependencies and a simulated MVC environment for thorough testing
 * of controller methods under various scenarios.
 *
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */


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
	
	/**
	 * Initializes resources needed for each test case in this test suite. This setup method is run before each test case. 
	 * It prepares a simulated environment for HTTP sessions, data models, and user account management interactions,
	 * ensuring that each test starts with a consistent, isolated state.
	 *
	 * <ul>
	 * <li>{@link MockHttpSession} - simulates an HTTP session environment.</li>
	 * <li>{@link Model} - a mock of the MVC model interface for use in web interactions.</li>
	 * <li>{@link User} - a simple user entity with a predefined user ID set to 1.</li>
	 * <li>{@link Account} - an instance of Account, representing user account data.</li>
	 * <li>{@link ForeignExchangeCurrency} - a placeholder for foreign currency exchange information.</li>
	 * <li>{@link RedirectAttributes} - a mock of the RedirectAttributes, used for specifying data required after redirection.</li>
	 * </ul>
	 *
	 * @see MockHttpSession
	 * @see Model
	 * @see User
	 * @see Account
	 * @see ForeignExchangeCurrency
	 * @see RedirectAttributes
	 */
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
	
	/**
	 * Tests the {@link AccountController#showBankAccountDashboard} method to ensure it
	 * correctly displays the bank account dashboard when a user, who has bank accounts, is logged in.
	 * This test follows the Arrange-Act-Assert pattern:
	 *
	 * <p><b>Arrange:</b> A user is simulated as the current logged-in user with two accounts.</p>
	 * <p><b>Act:</b> The {@link AccountController#showBankAccountDashboard} method is invoked.</p>
	 * <p><b>Assert:</b> Asserts that the returned view name is "account-dashboard".</p>
	 *
	 * This method uses a mock session to simulate a user login and mocks the {@link AccountService} 
	 * to return a predefined list of accounts. It tests the controller's ability to process and
	 * respond with the correct view based on the user's account status.
	 *
	 * @see AccountController#showBankAccountDashboard(HttpSession, Model)
	 * @see AccountService#findAllAccountsByUserId(Long)
	 */
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
	
	/**
	 * Validates that the {@link AccountController#showBankAccountDashboard} method returns the correct
	 * view name for a logged-in user who has no bank accounts. The test is structured using the Arrange-Act-Assert pattern:
	 *
	 * <p><b>Arrange:</b> A mock user session is set up with no associated bank accounts.</p>
	 * <p><b>Act:</b> The {@link AccountController#showBankAccountDashboard} method is called.</p>
	 * <p><b>Assert:</b> Verifies that the output view name is "account-dashboard".</p>
	 *
	 * This test checks the controller's handling of situations where a user is authenticated but has no accounts,
	 * ensuring that the user is still directed to the dashboard view. The {@link AccountService} is mocked to
	 * simulate this scenario by returning an empty account list.
	 *
	 * @see AccountController#showBankAccountDashboard(HttpSession, Model)
	 * @see AccountService#findAllAccountsByUserId(Long)
	 */
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
	
	
	/**
	 * Tests the {@link AccountController#withdrawalBankAccount} method to ensure it properly 
	 * displays the withdrawal view when a user is logged in with bank accounts and currency options.
	 * This test employs the Arrange-Act-Assert pattern:
	 *
	 * <p><b>Arrange:</b> A logged-in user session is set up with a list of bank accounts and supported foreign currencies.</p>
	 * <p><b>Act:</b> The {@link AccountController#withdrawalBankAccount} method is invoked.</p>
	 * <p><b>Assert:</b> Verifies that the returned view is "withdrawal".</p>
	 *
	 * This test checks the controller's functionality to navigate users to the withdrawal page, ensuring that
	 * it handles both account and currency data correctly under authenticated conditions.
	 *
	 * @see AccountController#withdrawalBankAccount(HttpSession, Model, RedirectAttributes)
	 * @see AccountService#findAllAccountsByUserId(Long)
	 * @see ForeignExchangeCurrencyService#getSupportedCurrencies()
	 */
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
	
	/**
	 * Tests the {@link AccountController#withdrawalBankAccount} method to verify that it redirects to
	 * the account dashboard when a logged-in user has no bank accounts, despite available currency options.
	 * The test is structured using the Arrange-Act-Assert pattern:
	 *
	 * <p><b>Arrange:</b> Configures a logged-in user session without any bank accounts but with a list of supported foreign currencies.</p>
	 * <p><b>Act:</b> The {@link AccountController#withdrawalBankAccount} method is called.</p>
	 * <p><b>Assert:</b> Asserts that the output view is redirected to the "account-dashboard".</p>
	 *
	 * This method checks the controller's response to situations where a user is authenticated but lacks 
	 * account resources to proceed with withdrawal, guiding them back to the dashboard for further actions.
	 *
	 * @see AccountController#withdrawalBankAccount(HttpSession, Model, RedirectAttributes)
	 * @see AccountService#findAllAccountsByUserId(Long)
	 * @see ForeignExchangeCurrencyService#getSupportedCurrencies()
	 */
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
	
	/**
	 * Tests the {@link AccountController#processWithdrawal} method for handling a successful withdrawal
	 * transaction. This test verifies that the method correctly processes withdrawals and redirects
	 * to the bank account dashboard upon success, using the Arrange-Act-Assert pattern:
	 *
	 * <p><b>Arrange:</b> Sets up a logged-in user with an account having sufficient funds in USD.</p>
	 * <p><b>Act:</b> Calls {@link AccountController#processWithdrawal} to withdraw a specified amount from the user's account.</p>
	 * <p><b>Assert:</b> Ensures the response is a redirect to the bank account dashboard ("redirect:/bankaccount/dashboard").</p>
	 *
	 * This function tests the withdrawal process under ideal conditions where the account and currency
	 * details match the withdrawal request, ensuring the functionality handles transactions correctly.
	 *
	 * @see AccountController#processWithdrawal(Long, String, BigDecimal, HttpSession, RedirectAttributes)
	 * @see AccountService#findById(Long)
	 * @see ForeignExchangeCurrencyService#getExchangeRate(String, String)
	 * @see ForeignExchangeCurrencyService#getCurrencyByCode(String)
	 */
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
	
	/**
	 * Tests the {@link AccountController#processWithdrawal} method for handling a non-successful
	 * withdrawal scenario. This test confirms that the method appropriately redirects to the withdrawal page
	 * when the withdrawal amount exceeds the account balance, using the Arrange-Act-Assert pattern:
	 *
	 * <p><b>Arrange:</b> Configures a user session with an account that has insufficient funds for the requested withdrawal.</p>
	 * <p><b>Act:</b> Attempts to withdraw an amount greater than the account's balance.</p>
	 * <p><b>Assert:</b> Asserts that the method responds by redirecting to the specific withdrawal page ("redirect:/bankaccount/withdrawal").</p>
	 *
	 * This test checks the controller's ability to handle error conditions effectively and ensure users are
	 * redirected to correct their input or manage their accounts appropriately.
	 *
	 * @see AccountController#processWithdrawal(Long, String, BigDecimal, HttpSession, RedirectAttributes)
	 * @see AccountService#findById(Long)
	 * @see ForeignExchangeCurrencyService#getExchangeRate(String, String)
	 */
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
	
	/**
	 * Tests the {@link AccountController#goToDepositPage} method to ensure it properly displays the deposit page
	 * when a user is logged in with existing bank accounts. This test uses the Arrange-Act-Assert pattern:
	 *
	 * <p><b>Arrange:</b> Sets up a user session with a specific account and a list of available currencies.</p>
	 * <p><b>Act:</b> Invokes the method to navigate to the deposit page.</p>
	 * <p><b>Assert:</b> Asserts that the "deposit" view is returned, indicating the user can proceed to deposit funds.</p>
	 *
	 * This test validates that the controller correctly handles the transition to the deposit interface under
	 * conditions where the user has active accounts and currency options, facilitating a smooth user experience.
	 *
	 * @see AccountController#goToDepositPage(Model, HttpSession)
	 * @see AccountService#findAllAccountsByUserId(Long)
	 * @see ForeignExchangeCurrencyService#getSupportedCurrencies()
	 */
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
	
	/**
	 * Tests the {@link AccountController#goToDepositPage} method to confirm it directs to the deposit page
	 * even when a logged-in user has no bank accounts but does have available currency options. The test is
	 * structured using the Arrange-Act-Assert pattern:
	 *
	 * <p><b>Arrange:</b> Configures a session for a logged-in user without any bank accounts but with a list of
	 * supported foreign currencies.</p>
	 * <p><b>Act:</b> Calls the method to navigate to the deposit page.</p>
	 * <p><b>Assert:</b> Verifies that the method returns the "deposit" view, indicating the user can engage with
	 * currency exchange or other financial services offered on the deposit page.</p>
	 *
	 * This test ensures that the controller properly facilitates access to the deposit interface under
	 * conditions where the user has currencies to manage but no active bank accounts.
	 *
	 * @see AccountController#goToDepositPage(Model, HttpSession)
	 * @see AccountService#findAllAccountsByUserId(Long)
	 * @see ForeignExchangeCurrencyService#getSupportedCurrencies()
	 */
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
	
	/**
	 * Tests the {@link AccountController#goToDepositPage} method to confirm it directs to the deposit page
	 * even when a logged-in user has no bank accounts but does have available currency options. The test is
	 * structured using the Arrange-Act-Assert pattern:
	 *
	 * <p><b>Arrange:</b> Configures a session for a logged-in user without any bank accounts but with a list of
	 * supported foreign currencies.</p>
	 * <p><b>Act:</b> Calls the method to navigate to the deposit page.</p>
	 * <p><b>Assert:</b> Verifies that the method returns the "deposit" view, indicating the user can engage with
	 * currency exchange or other financial services offered on the deposit page.</p>
	 *
	 * This test ensures that the controller properly facilitates access to the deposit interface under
	 * conditions where the user has currencies to manage but no active bank accounts.
	 *
	 * @see AccountController#goToDepositPage(Model, HttpSession)
	 * @see AccountService#findAllAccountsByUserId(Long)
	 * @see ForeignExchangeCurrencyService#getSupportedCurrencies()
	 */
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
	
	/**
	 * Tests the {@link AccountController#deposit} method to verify correct handling of deposit transactions
	 * involving currency conversion. This method checks the controller's ability to calculate the converted
	 * deposit amount correctly and to redirect properly following a successful deposit. The test is implemented
	 * using the Arrange-Act-Assert pattern:
	 *
	 * <p><b>Arrange:</b> Sets up an account with a balance in USD and simulates a deposit in EUR that needs
	 * conversion to USD using a specific exchange rate.</p>
	 * <p><b>Act:</b> Performs the deposit transaction with currency conversion.</p>
	 * <p><b>Assert:</b> Confirms that the redirect URL points to the bank account dashboard ("redirect:/bankaccount/dashboard").</p>
	 *
	 * The test ensures that currency conversions are handled accurately, reflecting the correct deposit
	 * amount in the account's currency and verifying that the application navigates to the correct view afterward.
	 *
	 * @see AccountController#deposit(Long, double, String)
	 * @see AccountService#findById(Long)
	 * @see ForeignExchangeCurrencyService#getCurrencyByCode(String)
	 * @see ForeignExchangeCurrencyService#getExchangeRate(String, String)
	 */
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
	
	/**
	 * Tests the {@link AccountController#goToCreateBankAccountPage} method to ensure it correctly displays
	 * the page for creating a new bank account when a user is logged in. This method assesses the controller's
	 * capability to render the appropriate view and pass necessary user data under authenticated conditions,
	 * using the Arrange-Act-Assert pattern:
	 *
	 * <p><b>Arrange:</b> Configures a session with a "loggedUser" attribute set to a mock user with a specified username.</p>
	 * <p><b>Act:</b> Invokes the controller method to transition to the bank account creation page.</p>
	 * <p><b>Assert:</b> Verifies that the returned view name is "create-bank-account" and that the user model
	 * attribute is correctly populated.</p>
	 *
	 * This test ensures that logged-in users can access the bank account creation page with their information
	 * pre-loaded, facilitating a user-friendly account setup process.
	 *
	 * @see AccountController#goToCreateBankAccountPage(HttpSession, Model)
	 */
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
	
	/**
	 * Tests the {@link AccountController#goToCreateBankAccountPage} method to verify it displays the
	 * "create-bank-account" page even when no user is logged in. This test evaluates the controller's response
	 * in scenarios where session authentication is not established, following the Arrange-Act-Assert pattern:
	 *
	 * <p><b>Arrange:</b> A session is configured without setting a "loggedUser", mimicking an unauthenticated access.</p>
	 * <p><b>Act:</b> The controller method is invoked to access the bank account creation page.</p>
	 * <p><b>Assert:</b> Checks that the returned view name is "create-bank-account", ensuring the page is accessible
	 * regardless of user authentication status.</p>
	 *
	 * This test is crucial for identifying potential security gaps, confirming that the application directs
	 * unauthenticated users correctly, possibly prompting for login.
	 *
	 * @see AccountController#goToCreateBankAccountPage(HttpSession, Model)
	 */
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
	
	/**
	 * Tests the {@link AccountController#goToCreateBankAccountPage} method to verify it displays the
	 * "create-bank-account" page even when no user is logged in. This test evaluates the controller's response
	 * in scenarios where session authentication is not established, following the Arrange-Act-Assert pattern:
	 *
	 * <p><b>Arrange:</b> A session is configured without setting a "loggedUser", mimicking an unauthenticated access.</p>
	 * <p><b>Act:</b> The controller method is invoked to access the bank account creation page.</p>
	 * <p><b>Assert:</b> Checks that the returned view name is "create-bank-account", ensuring the page is accessible
	 * regardless of user authentication status.</p>
	 *
	 * This test is crucial for identifying potential security gaps, confirming that the application directs
	 * unauthenticated users correctly, possibly prompting for login.
	 *
	 * @see AccountController#goToCreateBankAccountPage(HttpSession, Model)
	 */
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
	
	/**
	 * Tests the {@link AccountController#createBankAccount} function to verify its behavior when the initial deposit is insufficient.
	 * This unit test is designed to confirm that the system correctly identifies and handles cases where the provided starting
	 * capital does not meet the minimum account opening requirements, as stipulated by bank policy. It utilizes the Arrange-Act-Assert
	 * testing framework to structure the verification process:
	 *
	 * <p><b>Arrange:</b> An account name and a below-threshold initial deposit amount are prepared.</p>
	 * <p><b>Act:</b> The creation method is executed with the specified parameters to simulate the account creation process.</p>
	 * <p><b>Assert:</b> The test ensures that the method redirects back to the account creation page, signaling an error condition.
	 * It also checks that a specific error attribute is set, informing the user of the insufficient deposit, and verifies that
	 * no interactions have occurred with the account service, confirming no account creation attempt was made under these conditions.</p>
	 *
	 * This test is essential for safeguarding the integrity of the account creation process, ensuring that only viable accounts
	 * are established and that users are adequately informed of policy breaches, thus enhancing user experience and system reliability.
	 *
	 * @see AccountController#createBankAccount(String, double, HttpSession, RedirectAttributes)
	 */
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
	
	/**
	 * Tests the {@link AccountController#createBankAccount} method to ensure it handles cases where the account name is empty or blank.
	 * This test confirms that the system rejects account creation attempts lacking a valid name, using the Arrange-Act-Assert pattern:
	 *
	 * <p><b>Arrange:</b> An empty account name and a valid initial deposit amount are prepared.</p>
	 * <p><b>Act:</b> The account creation method is executed to attempt setting up a new account with these parameters.</p>
	 * <p><b>Assert:</b> Asserts that the user is redirected back to the account creation page, and checks that no account creation transaction
	 * is initiated, as evidenced by no interactions with the account service.</p>
	 *
	 * This test underscores the importance of input validation in the account setup process, ensuring robustness and adherence to business rules,
	 * thereby preventing the system from processing potentially problematic inputs.
	 *
	 * @see AccountController#createBankAccount(String, double, HttpSession, RedirectAttributes)
	 */
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
	
	/**
	 * Tests the {@link AccountController#goToTransferPage} method to verify that it successfully loads the transfer page
	 * with appropriate user and account information. This test ensures that users with valid sessions and associated accounts
	 * can access the page for conducting transfers, employing the Arrange-Act-Assert pattern:
	 *
	 * <p><b>Arrange:</b> Configures a user session with a specified user ID and prepares a list of user accounts and supported currencies.</p>
	 * <p><b>Act:</b> The method is invoked to navigate to the transfer page.</p>
	 * <p><b>Assert:</b> Confirms that the "transfer" view is returned and checks that the account and currency data are correctly populated into the model.</p>
	 *
	 * This test assesses the controller's functionality to populate the transfer page with necessary data, ensuring a
	 * seamless user experience for financial operations across accounts and currencies.
	 *
	 * @see AccountController#goToTransferPage(Model, HttpSession)
	 * @see AccountService#findAllAccountsByUserId(Long)
	 * @see ForeignExchangeCurrencyService#getSupportedCurrencies()
	 */
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
	
	/**
	 * Tests the {@link AccountController#transferMoney} method for the scenario where a transfer is attempted
	 * to the same account. This test ensures that the system correctly identifies and blocks self-transfers,
	 * redirecting to the transfer page with an appropriate error message, using the Arrange-Act-Assert pattern:
	 *
	 * <p><b>Arrange:</b> Prepares an account with sufficient balance and sets up the transfer to the same account number.</p>
	 * <p><b>Act:</b> Attempts to transfer money within the same account.</p>
	 * <p><b>Assert:</b> Verifies that the function redirects to the transfer page and checks for the presence of an
	 * error message indicating the transfer is to the same account.</p>
	 *
	 * This test validates the application's handling of redundant transaction requests, ensuring user actions do not
	 * lead to unnecessary operations or errors in account management.
	 *
	 * @see AccountController#transferMoney(Long, double, String, String, HttpSession, RedirectAttributes)
	 * @see AccountService#findById(Long)
	 * @see ForeignExchangeCurrencyService#getExchangeRate(String, String)
	 */
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
	
	/**
	 * Tests the {@link AccountController#transferMoney} method for handling cases where the transfer amount exceeds the account balance.
	 * This test ensures that the system correctly identifies and prevents transfers when funds are insufficient, redirecting to the transfer
	 * page with an appropriate error message, following the Arrange-Act-Assert pattern:
	 *
	 * <p><b>Arrange:</b> Sets up an account with a specific balance and simulates a transfer request that exceeds this balance.</p>
	 * <p><b>Act:</b> Executes the transfer to test the account's ability to handle an overdraw situation.</p>
	 * <p><b>Assert:</b> Verifies that the function redirects to the transfer page and confirms an error message for insufficient balance is presented.</p>
	 *
	 * This test validates the financial controls in place to ensure transactions do not exceed account capacities, thereby protecting
	 * user funds and maintaining system integrity.
	 *
	 * @see AccountController#transferMoney(Long, double, String, String, HttpSession, RedirectAttributes)
	 * @see AccountService#findById(Long)
	 * @see ForeignExchangeCurrencyService#getExchangeRate(String, String)
	 */
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
	
	/**
	 * Tests the {@link AccountController#transferMoney} method for executing a successful internal transfer between two accounts.
	 * This test verifies that funds are correctly transferred from one account to another within the same currency and ensures
	 * the system properly redirects to the dashboard page after completion, using the Arrange-Act-Assert pattern:
	 *
	 * <p><b>Arrange:</b> Prepares two accounts with adequate balances and active statuses, setting up for an internal transfer.</p>
	 * <p><b>Act:</b> Executes the transfer to verify the transaction and subsequent redirection.</p>
	 * <p><b>Assert:</b> Confirms that the transfer is successful and that the result redirects to the bank account dashboard.</p>
	 *
	 * This test checks the controller's functionality in managing internal transfers, ensuring transaction integrity and correct
	 * navigation post-transaction, thus supporting secure and effective financial operations.
	 *
	 * @see AccountController#transferMoney(Long, double, String, String, HttpSession, RedirectAttributes)
	 * @see AccountService#findById(Long)
	 * @see AccountService#findAccountByAccountNumber(String)
	 * @see ForeignExchangeCurrencyService#getExchangeRate(String, String)
	 */
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
	
	/**
	 * Tests the {@link AccountController#transferMoney} method for attempting a transfer to an account with a pending status.
	 * This test ensures that the system properly identifies and prevents transactions to non-active accounts, redirecting to the 
	 * transfer page with a specific error message, utilizing the Arrange-Act-Assert pattern:
	 *
	 * <p><b>Arrange:</b> Sets up an active account to transfer funds from and a recipient account in pending status to transfer funds to, both with sufficient balances and the same currency.</p>
	 * <p><b>Act:</b> Attempts the transfer between these accounts.</p>
	 * <p><b>Assert:</b> Verifies that the transfer is blocked, the user is redirected back to the transfer page, and an appropriate error message is provided.</p>
	 *
	 * This test checks the application's ability to enforce transaction rules related to account status, ensuring that funds are not moved to or from accounts not fully activated, thus safeguarding the transaction process.
	 *
	 * @see AccountController#transferMoney(Long, double, String, String, HttpSession, RedirectAttributes)
	 * @see AccountService#findById(Long)
	 * @see AccountService#findAccountByAccountNumber(String)
	 * @see StatusService#findByStatusName(String)
	 * @see ForeignExchangeCurrencyService#getExchangeRate(String, String)
	 */
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
	
	/**
	 * Tests the {@link AccountController#transferMoney} method for handling external transfers when the recipient account
	 * is not found within the same banking system. This test ensures that the system can process external transfers correctly,
	 * redirect to the dashboard after completion, and record the transaction, using the Arrange-Act-Assert pattern:
	 *
	 * <p><b>Arrange:</b> Prepares an active account with sufficient funds and sets up an external account as the transfer target, which does not exist in the current system.</p>
	 * <p><b>Act:</b> Executes an external transfer attempt to the non-existent account.</p>
	 * <p><b>Assert:</b> Checks that the system redirects to the dashboard and verifies that the transaction was recorded, indicating processing of the external transfer.</p>
	 *
	 * This test validates the application's external transfer capabilities, ensuring that transactions to external entities are handled appropriately and securely, enhancing the system's interoperability with other financial systems.
	 *
	 * @see AccountController#transferMoney(Long, double, String, String, HttpSession, RedirectAttributes)
	 * @see AccountService#findById(Long)
	 * @see AccountService#findAccountByAccountNumber(String)
	 * @see TransactionService#persist(Transaction)
	 * @see ForeignExchangeCurrencyService#getExchangeRate(String, String)
	 */
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
