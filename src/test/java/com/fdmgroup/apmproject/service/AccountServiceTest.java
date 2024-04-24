package com.fdmgroup.apmproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.repository.AccountRepository;

/**
 * Unit tests for the AccountService class. This class tests various
 * functionalities of the AccountService, such as its methods and interactions
 * with dependencies like AccountRepository and Logger. It uses Mockito to
 * create mock objects for dependencies and injects them into the AccountService
 * instance. Additionally, it sets up test data including Account and User
 * instances for testing purposes.
 *
 * @throws ExceptionType If any unexpected conditions occur during testing.
 * @see AccountService
 * @see AccountRepository
 * @see Logger
 */

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

	@Mock
	private AccountRepository accountRepo;

	@InjectMocks
	private AccountService accountService;

	private Account account;
	private User user;

	@Mock
	private static Logger logger = LogManager.getLogger(AccountService.class);

	/**
	 * Sets up the necessary objects and dependencies for testing the AccountService
	 * class. This method initializes mock objects for the AccountRepository and
	 * Logger, and creates a new Account and User instance for testing.
	 *
	 * @throws ExceptionType If the method encounters an unexpected condition during
	 *                       setup.
	 * @see AccountService
	 */
	@BeforeEach
	public void setUp() {
		account = new Account();
		account.setAccountId(12345L);
		account.setAccountName("Testing Account");
		account.setAccountNumber("123-456-789");
		user = new User();
	}

	/**
	 * Tests the successful creation of an account by verifying that the save
	 * function is called exactly once. This method sets up a scenario where an
	 * Account instance is created and passed to the AccountService. Using Mockito,
	 * it mocks the behavior of the AccountRepository's findById method to return an
	 * empty optional, simulating the absence of an existing account with the same
	 * ID. The persist method of the AccountService is then invoked with the newly
	 * created Account instance. Finally, it asserts that the save function of the
	 * AccountRepository is called exactly once with the new Account as an argument.
	 *
	 * @param param1 The Account instance representing the new account to be
	 *               created.
	 * @return No return value. The method verifies the behavior of the persist
	 *         method.
	 * @throws ExceptionType If the test encounters any unexpected conditions during
	 *                       execution.
	 * @see AccountService
	 * @see AccountRepository
	 */
	@Test
	@DisplayName("Test for successful account creation - save function should be called once")
	public void testPersistOne() {
		// Arrange
		Account newAccount = account;
		when(accountRepo.findById((long) 12345)).thenReturn(Optional.empty());

		// Act
		accountService.persist(newAccount);

		// Assert
		verify(accountRepo, times(1)).save(newAccount);
	}

	/**
	 * Tests the scenario where an account creation fails due to the existence of an
	 * account with the same ID. This method sets up a scenario where an Account
	 * instance is created and passed to the AccountService. Using Mockito, it mocks
	 * the behavior of the AccountRepository's findById method to return an optional
	 * containing the same Account instance, simulating the presence of an existing
	 * account with the same ID. The persist method of the AccountService is then
	 * invoked with the Account instance. Finally, it asserts that the save function
	 * of the AccountRepository is never called, ensuring that no duplicate accounts
	 * are created.
	 *
	 * @param param1 The Account instance representing the new account to be
	 *               created.
	 * @return No return value. The method verifies the behavior of the persist
	 *         method when a duplicate account exists.
	 * @throws ExceptionType If the test encounters any unexpected conditions during
	 *                       execution.
	 * @see AccountService
	 * @see AccountRepository
	 */
	@Test
	@DisplayName("Test for non-successful account creation")
	public void testPersistTwo() {
		// Arrange
		Account newAccount = account;
		when(accountRepo.findById((long) 12345)).thenReturn(Optional.of(newAccount));

		// Act
		accountService.persist(newAccount);

		// Assert
		verify(accountRepo, never()).save(any(Account.class));
	}

	/**
	 * Tests the scenario where an account is successfully updated. This method sets
	 * up a scenario where an existing Account instance is retrieved from the
	 * repository using its ID. It mocks the behavior of the AccountRepository's
	 * findById method to return an optional containing the existing Account
	 * instance. Then, the update method of the AccountService is invoked with the
	 * existing Account instance. Finally, it asserts that the save function of the
	 * AccountRepository is called exactly once with the existing Account instance,
	 * confirming that the update operation was successful.
	 *
	 * @param param1 The Account instance representing the existing account to be
	 *               updated.
	 * @return No return value. The method verifies the behavior of the update
	 *         method when the account exists.
	 * @throws ExceptionType If the test encounters any unexpected conditions during
	 *                       execution.
	 * @see AccountService
	 * @see AccountRepository
	 */
	@Test
	@DisplayName("Test for successful account update")
	public void testUpdateOne() {
		// Arrange
		Account existingAccount = account;
		when(accountRepo.findById((long) 12345)).thenReturn(Optional.of(existingAccount));

		// Act
		accountService.update(existingAccount);

		// Assert
		verify(accountRepo, times(1)).save(existingAccount);
	}

	/**
	 * Tests the scenario where an account update fails. This method sets up a
	 * scenario where an Account instance is attempted to be updated, but it does
	 * not exist in the repository. It mocks the behavior of the AccountRepository's
	 * findById method to return an empty optional. Then, the update method of the
	 * AccountService is invoked with the non-existing Account instance. Finally, it
	 * asserts that the save function of the AccountRepository is never called with
	 * the non-existing Account instance, confirming that no update operation was
	 * performed.
	 *
	 * @param param1 The Account instance representing the account that failed to be
	 *               updated.
	 * @return No return value. The method verifies the behavior of the update
	 *         method when the account does not exist.
	 * @throws ExceptionType If the test encounters any unexpected conditions during
	 *                       execution.
	 * @see AccountService
	 * @see AccountRepository
	 */
	@Test
	@DisplayName("Test for non-successful account update")
	public void testUpdateTwo() {
		// Arrange
		Account failedAccount = account;
		when(accountRepo.findById((long) 12345)).thenReturn(Optional.empty());

		// Act
		accountService.update(failedAccount);

		// Assert
		verify(accountRepo, never()).save(failedAccount);
	}

	/**
	 * Tests the successful finding of a bank account by AccountID. This method sets
	 * up a scenario where an Account instance is expected to be found in the
	 * repository based on its AccountID. It mocks the behavior of the
	 * AccountRepository's findById method to return an optional containing the
	 * existing Account instance. Then, it invokes the findById method of the
	 * AccountService with the AccountID of the existing Account. Finally, it
	 * asserts that the returned Account instance is not null and matches the
	 * expected existing Account instance.
	 *
	 * @param param1 The Account instance representing the account to be found.
	 * @return The found Account instance.
	 * @throws ExceptionType If the test encounters any unexpected conditions during
	 *                       execution.
	 * @see AccountService
	 * @see AccountRepository
	 */
	@Test
	@DisplayName("Test for successful finding of Bank Account by AccountID")
	public void testFindAccountByAccountIDOne() {
		// Arrange
		Account existingAccount = account;
		when(accountRepo.findById(existingAccount.getAccountId())).thenReturn(Optional.of(existingAccount));

		// Act
		Account actualAccount = accountService.findById(existingAccount.getAccountId());

		// Assert
		assertNotNull(actualAccount);
		assertEquals(existingAccount, actualAccount);
	}

	/**
	 * Tests the failure to find a bank account by AccountID. This method sets up a
	 * scenario where no Account instance is found in the repository based on the
	 * provided AccountID. It mocks the behavior of the AccountRepository's findById
	 * method to return an empty optional when invoked with the provided AccountID.
	 * Then, it invokes the findById method of the AccountService with the
	 * AccountID. Finally, it asserts that the returned Account instance is null.
	 *
	 * @param param1 The Account ID used for searching the account.
	 * @return Null since no account is found with the provided Account ID.
	 * @throws ExceptionType If the test encounters any unexpected conditions during
	 *                       execution.
	 * @see AccountService
	 * @see AccountRepository
	 */
	@SuppressWarnings("null")
	@Test
	@DisplayName("Test for failure to find Bank Account by AccountID")
	public void testFindAccountByAccountIDTwo() {
		// Arrange
		long accountId = 45678L;
		when(accountRepo.findById(accountId)).thenReturn(Optional.empty());

		// Act
		Account actualAccount = accountService.findById(accountId);

		// Assert
		assertNull(actualAccount);
	}

	/**
	 * Tests the successful deletion of a bank account by AccountID. This method
	 * verifies that the deleteById method of the AccountRepository is called once
	 * with the AccountID of the account to be deleted. It sets up a scenario where
	 * the Account instance to be deleted is found in the repository based on the
	 * provided AccountID. It mocks the behavior of the AccountRepository's findById
	 * method to return an optional containing the Account instance when invoked
	 * with the provided AccountID. Then, it invokes the deleteById method of the
	 * AccountService with the AccountID. Finally, it verifies that the deleteById
	 * method of the AccountRepository is invoked exactly once with the provided
	 * AccountID.
	 *
	 * @param param1 The Account ID of the account to be deleted.
	 * @return No return value.
	 * @throws ExceptionType If the test encounters any unexpected conditions during
	 *                       execution.
	 * @see AccountService
	 * @see AccountRepository
	 */
	@Test
	@DisplayName("Test for successful account deletion")
	public void testAccountDeletionByAccountIDOne() {
		// Arrange
		Account deletionAccount = account;
		when(accountRepo.findById(deletionAccount.getAccountId())).thenReturn(Optional.of(deletionAccount));

		// Act
		accountService.deleteById(deletionAccount.getAccountId());

		// Assert
		verify(accountRepo, times(1)).deleteById(deletionAccount.getAccountId());
	}

	/**
	 * Tests the account deletion when the account does not exist. This method
	 * ensures that the deleteById method of the AccountRepository is never called
	 * when attempting to delete a non-existing account. It sets up a scenario where
	 * the Account instance with the provided AccountID is not found in the
	 * repository. It mocks the behavior of the AccountRepository's findById method
	 * to return an empty optional when invoked with the provided AccountID. Then,
	 * it invokes the deleteById method of the AccountService with the AccountID.
	 * Finally, it verifies that the deleteById method of the AccountRepository is
	 * never invoked with any AccountID.
	 *
	 * @param accountID The Account ID of the non-existing account to be deleted.
	 * @return No return value.
	 * @throws ExceptionType If the test encounters any unexpected conditions during
	 *                       execution.
	 * @see AccountService
	 * @see AccountRepository
	 */
	@Test
	@DisplayName("Test for account deletion on non-existing account")
	public void testAccountDeletionByAccountIDTwo() {
		// Arrange
		long accountID = 111L;
		when(accountRepo.findById(accountID)).thenReturn(Optional.empty());

		// Act
		accountService.deleteById(accountID);

		// Assert
		verify(accountRepo, never()).deleteById(anyLong());
	}

	/**
	 * Tests the successful retrieval of an account by its account number. This
	 * method verifies that the AccountService's findAccountByAccountNumber method
	 * correctly retrieves an account from the repository when provided with a valid
	 * account number. It sets up a scenario where the Account instance with the
	 * provided account number is found in the repository. It mocks the behavior of
	 * the AccountRepository's findByAccountNumber method to return an optional
	 * containing the expected Account instance when invoked with the provided
	 * account number. Then, it invokes the findAccountByAccountNumber method of the
	 * AccountService with the account number. Finally, it asserts that the
	 * retrieved account is not null and equals the expected account.
	 *
	 * @param accountNumber The account number of the account to be retrieved.
	 * @return No return value.
	 * @throws ExceptionType If the test encounters any unexpected conditions during
	 *                       execution.
	 * @see AccountService
	 * @see AccountRepository
	 */
	@Test
	@DisplayName("Test for successful find account by Account Number")
	public void testFindAccountByAccountNumberOne() {
		// Arrange
		Account expectedAccount = account;
		String accountNumber = expectedAccount.getAccountNumber();
		when(accountRepo.findByAccountNumber(accountNumber)).thenReturn(Optional.of(expectedAccount));

		// Act
		Account retrievedAccount = accountService.findAccountByAccountNumber(accountNumber);

		// Assert
		assertNotNull(retrievedAccount);
		assertEquals(expectedAccount, retrievedAccount);
	}

	/**
	 * Tests the finding of an account with a non-existing account number. This
	 * method verifies that the AccountService's findAccountByAccountNumber method
	 * returns null when attempting to retrieve an account with a non-existing
	 * account number. It sets up a scenario where no Account instance is found in
	 * the repository for the provided account number. It mocks the behavior of the
	 * AccountRepository's findByAccountNumber method to return an empty optional
	 * when invoked with the provided account number. Then, it invokes the
	 * findAccountByAccountNumber method of the AccountService with the non-existing
	 * account number. Finally, it asserts that the retrieved account is null.
	 *
	 * @param accountNumber The account number of the non-existing account.
	 * @return No return value.
	 * @throws ExceptionType If the test encounters any unexpected conditions during
	 *                       execution.
	 * @see AccountService
	 * @see AccountRepository
	 */
	@Test
	@DisplayName("Test for finding account for non-existing account number")
	public void testFindAccountByAccountNumberTwo() {
		// Arrange
		String accountNumber = "1234-5555-1111";
		when(accountRepo.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());

		// Act
		Account retrievedAccount = accountService.findAccountByAccountNumber(accountNumber);

		// Assert
		assertNull(retrievedAccount);
	}

	/**
	 * Tests a normal withdrawal scenario where the withdrawal amount is less than
	 * the account balance. This method verifies that the AccountService's
	 * withdrawAccountByAmount method correctly updates the account balance after a
	 * withdrawal. It sets up a scenario where the initial account balance is
	 * $100.00 and the withdrawal amount is $50.00. Then, it invokes the
	 * withdrawAccountByAmount method of the AccountService with these parameters.
	 * Finally, it asserts that the resulting balance after the withdrawal is as
	 * expected, which should be $50.00.
	 *
	 * @param accountBalance   The initial balance of the account before withdrawal.
	 * @param withdrawalAmount The amount to be withdrawn from the account.
	 * @return The resulting balance after the withdrawal.
	 * @throws ExceptionType If the test encounters any unexpected conditions during
	 *                       execution.
	 * @see AccountService
	 */
	@Test
	@DisplayName("Test for normal withdrawal where withdrawal amount less than account balance")
	public void testWithdrawAccountOne() {
		// Arrange
		BigDecimal accountBalance = new BigDecimal("100.00");
		BigDecimal withdrawalAmount = new BigDecimal("50.00");

		// Act
		double result = accountService.withdrawAccountByAmount(accountBalance, withdrawalAmount);

		// Assert
		assertEquals(50.00, result, 0.001, "The balance after withdrawal should be correct!");
	}

	/**
	 * Tests a withdrawal scenario where the withdrawal amount equals the account
	 * balance. This method verifies that the AccountService's
	 * withdrawAccountByAmount method correctly handles a withdrawal where the
	 * withdrawal amount is equal to the account balance. It sets up a scenario
	 * where the initial account balance is $100.00 and the withdrawal amount is
	 * $100.00. Then, it invokes the withdrawAccountByAmount method of the
	 * AccountService with these parameters. Finally, it asserts that the resulting
	 * balance after the withdrawal is as expected, which should be $0.00.
	 *
	 * @param accountBalance   The initial balance of the account before withdrawal.
	 * @param withdrawalAmount The amount to be withdrawn from the account.
	 * @return The resulting balance after the withdrawal.
	 * @throws ExceptionType If the test encounters any unexpected conditions during
	 *                       execution.
	 * @see AccountService
	 */
	@Test
	@DisplayName("Test for normal withdrawal where withdrawal amount equal to account balance")
	public void testWithdrawAccountTwo() {
		// Arrange
		BigDecimal accountBalance = new BigDecimal("100.00");
		BigDecimal withdrawalAmount = new BigDecimal("100.00");

		// Act
		double result = accountService.withdrawAccountByAmount(accountBalance, withdrawalAmount);

		// Assert
		assertEquals(0, result, 0.001, "The balance after withdrawal should be correct!");
	}

	/**
	 * Tests a withdrawal scenario where the withdrawal amount exceeds the account
	 * balance. This method verifies that the AccountService's
	 * withdrawAccountByAmount method correctly handles a withdrawal where the
	 * withdrawal amount is greater than the account balance. It sets up a scenario
	 * where the initial account balance is $100.00 and the withdrawal amount is
	 * $150.00. Then, it invokes the withdrawAccountByAmount method of the
	 * AccountService with these parameters. Finally, it asserts that the resulting
	 * balance after the withdrawal is negative, indicating that the withdrawal
	 * could not be completed due to insufficient funds.
	 *
	 * @param accountBalance   The initial balance of the account before withdrawal.
	 * @param withdrawalAmount The amount to be withdrawn from the account.
	 * @return The resulting balance after the withdrawal.
	 * @throws ExceptionType If the test encounters any unexpected conditions during
	 *                       execution.
	 * @see AccountService
	 */
	@Test
	@DisplayName("Test for normal withdrawal where withdrawal amount more than account balance")
	public void testWithdrawAccountThree() {
		// Arrange
		BigDecimal accountBalance = new BigDecimal("100.00");
		BigDecimal withdrawalAmount = new BigDecimal("150.00");

		// Act
		double result = accountService.withdrawAccountByAmount(accountBalance, withdrawalAmount);

		// Assert
		assertTrue(result < 0, "The balance after withdrawal should be correct!");
	}

	/**
	 * Tests the functionality of finding accounts associated with a specified user
	 * ID when there are two accounts. This method verifies that the
	 * AccountService's findAllAccountsByUserId method correctly retrieves all
	 * accounts linked to a specified user ID when there are two accounts associated
	 * with that user. It prepares a scenario where the user has a user ID of 111
	 * and two accounts associated with it. Then, it mocks the behavior of the
	 * account repository to return the expected accounts for the given user ID. The
	 * method then invokes the findAllAccountsByUserId method of the AccountService
	 * and asserts that the returned list of accounts matches the expected list of
	 * accounts.
	 *
	 * @param userId The user ID for which to retrieve the associated accounts.
	 * @return The list of accounts associated with the specified user ID.
	 * @throws ExceptionType If the test encounters any unexpected conditions during
	 *                       execution.
	 * @see AccountService
	 */
	@Test
	@DisplayName("Test for Find Accounts By UserId when there's 2 accounts")
	public void testFindAllAccountsForSpecifiedUserOne() {
		// Arrange
		User currentUser = user;
		currentUser.setUserId(111L);
		currentUser.setAccounts(new Account());
		currentUser.setAccounts(new Account());
		List<Account> expectedAccounts = currentUser.getAccounts();
		when(accountRepo.findByAccountUserUserId(currentUser.getUserId())).thenReturn(expectedAccounts);

		// Act
		List<Account> actualAccounts = accountService.findAllAccountsByUserId(currentUser.getUserId());

		// Assert
		assertNotNull(actualAccounts);
		assertEquals(expectedAccounts, actualAccounts, "The returned accounts should match");
	}

	/**
	 * Tests the functionality of finding all accounts associated with a specified
	 * user ID when there are no accounts. This method verifies that the
	 * AccountService's findAllAccountsByUserId method correctly handles the
	 * scenario where there are no accounts associated with a specified user ID. It
	 * prepares a scenario where the user has a user ID of 111 but no associated
	 * accounts. Then, it mocks the behavior of the account repository to return an
	 * empty list of accounts for the given user ID. The method then invokes the
	 * findAllAccountsByUserId method of the AccountService and asserts that the
	 * returned list of accounts is empty, indicating no accounts found for the
	 * specified user ID.
	 *
	 * @param userId The user ID for which to retrieve the associated accounts.
	 * @return The list of accounts associated with the specified user ID.
	 * @throws ExceptionType If the test encounters any unexpected conditions during
	 *                       execution.
	 * @see AccountService
	 */
	@Test
	@DisplayName("Test for Find All accounts when there is no bank account under user")
	public void testFindAllAccountsForSpecifiedUserTwo() {
		// Arrange
		User currentUser = user;
		currentUser.setUserId(111L);
		List<Account> expectedAccounts = currentUser.getAccounts();
		when(accountRepo.findByAccountUserUserId(currentUser.getUserId())).thenReturn(new ArrayList<>());

		// Act
		List<Account> actualAccounts = accountService.findAllAccountsByUserId(currentUser.getUserId());

		// Assert
		assertNotNull(actualAccounts);
		assertTrue(actualAccounts.isEmpty(), "The list should be empty.");
	}

	/**
	 * Tests the functionality of retrieving a list of all accounts. This method
	 * verifies that the AccountService's getAllAccounts method correctly retrieves
	 * and returns a list of all accounts. It prepares a scenario where there are
	 * two accounts in the system and sets up the account repository mock to return
	 * these accounts when the findAll method is called. The method then invokes the
	 * getAllAccounts method of the AccountService and asserts that the returned
	 * list of accounts matches the expected list.
	 *
	 * @return The list of all accounts retrieved by the getAllAccounts method.
	 * @throws ExceptionType If the test encounters any unexpected conditions during
	 *                       execution.
	 * @see AccountService
	 */
	@Test
	@DisplayName("Test for retrieving a list of all accounts")
	public void testFindAllAccountsOne() {
		// Arrange
		List<Account> expectedAccounts = new ArrayList<>();
		expectedAccounts.add(new Account());
		expectedAccounts.add(new Account());
		when(accountRepo.findAll()).thenReturn(expectedAccounts);

		// Act
		List<Account> actualAccounts = accountService.getAllAccounts();

		// Assert
		assertNotNull(actualAccounts);
		assertEquals(expectedAccounts, actualAccounts, "The returned accounts should match");
	}

	/**
	 * Tests the functionality of retrieving a list of empty accounts. This method
	 * verifies that the AccountService's getAllAccounts method behaves correctly
	 * when there are no accounts in the system. It prepares a scenario where the
	 * account repository mock returns an empty list when the findAll method is
	 * called. The method then invokes the getAllAccounts method of the
	 * AccountService and asserts that the returned list of accounts is empty.
	 *
	 * @return An empty list of accounts retrieved by the getAllAccounts method.
	 * @throws ExceptionType If the test encounters any unexpected conditions during
	 *                       execution.
	 * @see AccountService
	 */
	@Test
	@DisplayName("Test for retrieving a list of empty accounts")
	public void testFindAllAccountsTo() {
		// Arrange
		List<Account> expectedAccounts = new ArrayList<>();
		when(accountRepo.findAll()).thenReturn(expectedAccounts);

		// Act
		List<Account> actualAccounts = accountService.getAllAccounts();

		// Assert
		assertNotNull(actualAccounts);
		assertTrue(actualAccounts.isEmpty(), "The returned list should have no accounts");
	}

}
