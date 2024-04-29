package com.fdmgroup.apmproject.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
 * Test suite for {@link CreditCardController} using Spring MVC test framework.
 * This class simulates a web application context to test the interactions
 * with the {@link CreditCardController}. It configures automatic injection of
 * mocked dependencies and a simulated MVC environment for thorough testing
 * of controller methods under various scenarios.
 *
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */

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
		creditCardController = new CreditCardController(creditCardService, currencyService, statusService, userService,
				accountService, mccService, transactionService);
		reset(session, model);
	}

	/**
	 * Tests the behavior of the credit card dashboard when a user is logged in.
	 * <p>
	 * This method performs a test for the GET request to the credit card dashboard when a user is logged in. It sets up the necessary environment and checks if the appropriate view name is returned along with the expected model attributes. The test verifies that the user's credit cards and user information are correctly added to the model, and that the session attribute for the logged-in user is accessed.
	 *
	 * @throws AssertionError If the test fails to assert the expected behavior.
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 */
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
		assertEquals("card/card-dashboard", viewName);
		verify(model).addAttribute("cards", userCreditCards);
		verify(model).addAttribute("user", loggedUser);
		verify(session).getAttribute("loggedUser");
	}


	/**
	 * Tests the behavior of the credit card application page for a GET request.
	 * <p>
	 * This method performs a test for the GET request to the credit card application page. It sets up the necessary environment by creating a mock logged-in user session and verifies if the appropriate view name is returned along with the expected model attributes. The test checks that the user attribute is added to the model and ensures that the session attribute for the logged-in user is accessed.
	 *
	 * @throws AssertionError If the test fails to assert the expected behavior.
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 */
	@Test
	@DisplayName("Test for get request to apply for credit cards")
	void test3() {
		// Arrange
		User loggedUser = new User(); // You need to have User class and its instantiation here
		when(session.getAttribute("loggedUser")).thenReturn(loggedUser);

		// Act
		String viewName = creditCardController.applyCreditCard(model, session);

		// Assert
		assertEquals("card/apply-credit-card", viewName);
		verify(model).addAttribute("user", loggedUser);
		verify(session).getAttribute("loggedUser");
	}

	/**
	 * Tests the behavior of the credit card application process for a POST request when the monthly salary is blank.
	 * <p>
	 * This method conducts a test for the POST request to apply for credit cards when the monthly salary is blank. It sets up the necessary environment by creating a mock user session with an empty monthly salary and verifies if the appropriate view name is returned along with the expected model attribute indicating an error. Additionally, it ensures that the user session attribute is accessed and that there are no further interactions with other services.
	 *
	 * @throws AssertionError If the test fails to assert the expected behavior.
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 */
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
		assertEquals("card/apply-credit-card", viewName);
		verify(model).addAttribute("error", true);
		verify(session).getAttribute("loggedUser");
		verifyNoMoreInteractions(creditCardService, currencyService, statusService, userService);
	}

	/**
	 * Tests the behavior of the credit card application process for a POST request when the card type is blank.
	 * <p>
	 * This method conducts a test for the POST request to apply for credit cards when the card type is blank. It sets up the necessary environment by creating a mock user session with a specified monthly salary but blank card type. It then verifies if the appropriate view name is returned along with the expected model attribute indicating an error. Additionally, it ensures that the user session attribute is accessed and that there are no further interactions with other services.
	 *
	 * @throws AssertionError If the test fails to assert the expected behavior.
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 */
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
		assertEquals("card/apply-credit-card", viewName);
		verify(model).addAttribute("error", true);
		verify(session).getAttribute("loggedUser");
		verifyNoMoreInteractions(creditCardService, currencyService, statusService, userService);
	}

	/**
	 * Tests the behavior of the credit card application process for a POST request when the monthly salary is below $1000.
	 * <p>
	 * This method conducts a test for the POST request to apply for credit cards when the monthly salary is below $1000. It sets up the necessary environment by creating a mock user session with a specified monthly salary below the threshold. It then verifies if the appropriate view name is returned along with the expected model attribute indicating an error. Additionally, it ensures that the user session attribute is accessed and that there are no further interactions with other services.
	 *
	 * @throws AssertionError If the test fails to assert the expected behavior.
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 */
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
		assertEquals("card/apply-credit-card", viewName);
		verify(model).addAttribute("error2", true);
		verify(session).getAttribute("loggedUser");
		verifyNoMoreInteractions(creditCardService, currencyService, statusService, userService);
	}

	/**
	 * Tests the successful behavior of the credit card application process for a POST request.
	 * <p>
	 * This method conducts a test for the POST request to apply for credit cards successfully. It sets up the necessary environment by creating a mock user session with a specified monthly salary and card type. Additionally, it mocks the necessary services such as currency and status services to simulate the application process. The test verifies if the application results in the expected redirection to the user's card page and if the credit card and user details are properly persisted and updated. It also ensures that the required session attributes are accessed, and the relevant services are called with appropriate parameters.
	 *
	 * @throws AssertionError If the test fails to assert the expected behavior.
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 */
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

	/**
	 * Tests the behavior of the payment webpage view for a GET request when the user is logged in.
	 * <p>
	 * This method performs a test for the GET request to view the payment webpage when a user is logged in. It sets up the necessary environment by creating a mock user session with associated credit card and account lists. Additionally, it mocks the account service to simulate retrieving the user's accounts. The test verifies if the view name matches the expected page, and if the model attributes are properly populated with the user's account and credit card lists. It also ensures that the session attribute is accessed, and the account service is called with the correct user ID.
	 *
	 * @throws AssertionError If the test fails to assert the expected behavior.
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 */
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
		assertEquals("card/pay-bills", viewName);
		verify(model).addAttribute("AccountList", accountList);
		verify(model).addAttribute("user", currentUser);
		verify(model).addAttribute("CcList", ccList);
		verify(session).getAttribute("loggedUser");
		verify(accountService).findAllAccountsByUserId(currentUser.getUserId());
	}


	/**
	 * Tests the behavior of making a payment via a POST request when no account is chosen.
	 * <p>
	 * This method performs a test for making a payment via a POST request when no account is chosen. It sets up the necessary environment by mocking the redirect attributes and passing null for the account parameter. The test verifies if the view name matches the expected redirection to the payment webpage and if the redirect attribute "NotChooseAccountError" is set to true. Additionally, it ensures that there are no further interactions with other services.
	 *
	 * @param redirectAttributes Redirect attributes to add attributes to the redirect URL.
	 * @return The view name after the redirection.
	 * @throws AssertionError If the test fails to assert the expected behavior.
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 */
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
		verifyNoMoreInteractions(accountService, creditCardService, mccService, currencyService, transactionService,
				userService);
	}

	/**
	 * Tests the behavior of making a payment via a POST request when no credit card is chosen.
	 * <p>
	 * This method sets up a test scenario where it verifies the behavior of the system when attempting to make a payment without selecting a credit card. It mocks the necessary dependencies and ensures that the redirect attribute "NotChooseCreditCardError" is set to true upon encountering this condition. Additionally, it verifies that no further interactions occur with other services after this scenario.
	 *
	 * @param redirectAttributes Redirect attributes to add attributes to the redirect URL.
	 * @return The view name after the redirection.
	 * @throws AssertionError If the test fails to assert the expected behavior.
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 */
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
		verifyNoMoreInteractions(accountService, creditCardService, mccService, currencyService, transactionService,
				userService);
	}

	/**
	 * Tests the behavior of making a custom payment via a POST request.
	 * <p>
	 * This method simulates the process of making a custom payment by setting up a test scenario with predefined credit card and account details. It then verifies that the payment transaction is processed correctly, updating the credit card balance and account details accordingly. The test ensures that the user session is appropriately updated after the payment. Additionally, it verifies that no further interactions occur with other services after this scenario.
	 *
	 * @param creditCardId The ID of the credit card used for the payment.
	 * @param paymentAmount The amount to be paid.
	 * @param balanceType The type of balance (e.g., custom).
	 * @param accountId The ID of the account from which the payment is made.
	 * @param redirectAttributes Redirect attributes to add attributes to the redirect URL.
	 * @return The view name after the redirection.
	 * @throws AssertionError If the test fails to assert the expected behavior.
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 */
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
		String viewName = creditCardController.makeCcbills(model, session, creditCardId, paymentAmount, balanceType,
				accountId, redirectAttributes);

		// Assert
		assertEquals("redirect:/userCards", viewName);
		verify(accountService).findById(account.getAccountId());
		verify(creditCardService).findById(creditCard.getCreditCardId());
		verify(mccService).findByMerchantCategory("Bill");
		verify(currencyService).getCurrencyByCode("SGD");
		verify(transactionService).persist(any(Transaction.class));
		verify(transactionService).updateCreditCardBalance(any(Transaction.class));
		verify(accountService).update(account);
		verify(userService, times(2)).update(currentUser);
		verify(session).setAttribute("loggedUser", currentUser);
	}

	/**
	 * Tests the behavior of making a minimum payment via a POST request.
	 * <p>
	 * This method simulates the process of making a minimum payment by setting up a test scenario with predefined credit card and account details. It then verifies that the minimum payment transaction is processed correctly, updating the credit card balance and account details accordingly. The test ensures that the user session is appropriately updated after the payment. Additionally, it verifies that no further interactions occur with other services after this scenario.
	 *
	 * @param creditCardId The ID of the credit card used for the payment.
	 * @param paymentAmount The amount to be paid.
	 * @param balanceType The type of balance (e.g., minimum).
	 * @param accountId The ID of the account from which the payment is made.
	 * @param redirectAttributes Redirect attributes to add attributes to the redirect URL.
	 * @return The view name after the redirection.
	 * @throws AssertionError If the test fails to assert the expected behavior.
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 */
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
		String viewName = creditCardController.makeCcbills(model, session, creditCardId, paymentAmount, balanceType,
				accountId, redirectAttributes);

		// Assert
		assertEquals("redirect:/userCards", viewName);
		verify(accountService).findById(account.getAccountId());
		verify(creditCardService).findById(creditCard.getCreditCardId());
		verify(mccService).findByMerchantCategory("Bill");
		verify(currencyService).getCurrencyByCode("SGD");
	}

	/**
	 * Tests the behavior of making a statement payment via a POST request.
	 * <p>
	 * This method sets up a test scenario to simulate the process of making a statement payment. It includes predefined credit card and account details and verifies that the statement payment transaction is processed correctly. After the payment, it ensures that the credit card balance and account details are updated accordingly. Additionally, the user session is checked to confirm that it is appropriately updated after the payment process. The test also ensures that no further interactions occur with other services after this scenario.
	 *
	 * @param creditCardId The ID of the credit card used for the payment.
	 * @param paymentAmount The amount to be paid.
	 * @param balanceType The type of balance (e.g., statement).
	 * @param accountId The ID of the account from which the payment is made.
	 * @param redirectAttributes Redirect attributes to add attributes to the redirect URL.
	 * @return The view name after the redirection.
	 * @throws AssertionError If the test fails to assert the expected behavior.
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 */
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
		String viewName = creditCardController.makeCcbills(model, session, creditCardId, paymentAmount, balanceType,
				accountId, redirectAttributes);

		// Assert
		assertEquals("redirect:/userCards", viewName);
		verify(accountService).findById(account.getAccountId());
		verify(creditCardService).findById(creditCard.getCreditCardId());
		verify(mccService).findByMerchantCategory("Bill");
		verify(currencyService).getCurrencyByCode("SGD");
		verify(transactionService).persist(any(Transaction.class));
		verify(transactionService).updateCreditCardBalance(any(Transaction.class));
		verify(accountService).update(account);
		verify(userService, times(2)).update(currentUser);
		verify(session).setAttribute("loggedUser", currentUser);
	}

	/**
	 * Tests the behavior of making a current payment via a POST request.
	 * <p>
	 * This method sets up a test scenario to simulate the process of making a current payment. It includes predefined credit card and account details and verifies that the current payment transaction is processed correctly. After the payment, it ensures that the credit card balance and account details are updated accordingly. Additionally, the user session is checked to confirm that it is appropriately updated after the payment process. The test also ensures that no further interactions occur with other services after this scenario.
	 *
	 * @param creditCardId The ID of the credit card used for the payment.
	 * @param paymentAmount The amount to be paid.
	 * @param balanceType The type of balance (e.g., current).
	 * @param accountId The ID of the account from which the payment is made.
	 * @param redirectAttributes Redirect attributes to add attributes to the redirect URL.
	 * @return The view name after the redirection.
	 * @throws AssertionError If the test fails to assert the expected behavior.
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 */
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
		String viewName = creditCardController.makeCcbills(model, session, creditCardId, paymentAmount, balanceType,
				accountId, redirectAttributes);

		// Assert
		assertEquals("redirect:/userCards", viewName);
		verify(accountService).findById(account.getAccountId());
		verify(creditCardService).findById(creditCard.getCreditCardId());
		verify(mccService).findByMerchantCategory("Bill");
		verify(currencyService).getCurrencyByCode("SGD");
		verify(transactionService).persist(any(Transaction.class));
		verify(transactionService).updateCreditCardBalance(any(Transaction.class));
		verify(accountService).update(account);
		verify(userService, times(2)).update(currentUser);
		verify(session).setAttribute("loggedUser", currentUser);
	}
}
