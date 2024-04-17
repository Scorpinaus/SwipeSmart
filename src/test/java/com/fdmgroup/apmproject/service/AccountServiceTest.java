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

	@BeforeEach
	public void setUp() {
		account = new Account();
		account.setAccountId(12345L);
		account.setAccountName("Testing Account");
		account.setAccountNumber("123-456-789");
		user = new User();
	}
	
	@Test
	@DisplayName("Test for successful account creation - save function should be called once")
	public void testPersistOne() {
	//Arrange
		Account newAccount = account;
		when(accountRepo.findById((long) 12345)).thenReturn(Optional.empty());
		
	//Act
		accountService.persist(newAccount);
		
	//Assert
		verify(accountRepo, times(1)).save(newAccount);
	}
	
	@Test
	@DisplayName("Test for non-successful account creation")
	public void testPersistTwo() {
		//Arrange
		Account newAccount = account;
		when(accountRepo.findById((long) 12345)).thenReturn(Optional.of(newAccount));
		
		//Act
		accountService.persist(newAccount);
		
		//Assert
		verify(accountRepo, never()).save(any(Account.class));
	}
	
	@Test
	@DisplayName("Test for successful account update")
	public void testUpdateOne() {
		//Arrange
		Account existingAccount = account;
		when(accountRepo.findById((long) 12345)).thenReturn(Optional.of(existingAccount));
		
		//Act
		accountService.update(existingAccount);
		
		//Assert
		verify(accountRepo, times(1)).save(existingAccount);
	}
	
	@Test
	@DisplayName("Test for non-successful account update")
	public void testUpdateTwo() {
		//Arrange
		Account failedAccount = account;
		when(accountRepo.findById((long) 12345)).thenReturn(Optional.empty());
		
		//Act
		accountService.update(failedAccount);
		
		//Assert
		verify(accountRepo, never()).save(failedAccount);
	}
	
	@Test
	@DisplayName("Test for successful finding of Bank Account by AccountID")
	public void testFindAccountByAccountIDOne() {
		//Arrange
		Account existingAccount = account;
		when(accountRepo.findById(existingAccount.getAccountId())).thenReturn(Optional.of(existingAccount));
		
		//Act
		Account actualAccount = accountService.findById(existingAccount.getAccountId());
		
		//Assert
		assertNotNull(actualAccount);
		assertEquals(existingAccount, actualAccount);
	}
	
	@SuppressWarnings("null")
	@Test
	@DisplayName("Test for failure to find Bank Account by AccountID")
	public void testFindAccountByAccountIDTwo() {
		//Arrange
		long accountId = 45678L;
		when(accountRepo.findById(accountId)).thenReturn(Optional.empty());
		
		//Act
		Account actualAccount = accountService.findById(accountId);
		
		//Assert
		assertNull(actualAccount);
	}
	
	@Test
	@DisplayName("Test for successful account deletion")
	public void testAccountDeletionByAccountIDOne() {
		//Arrange
		Account deletionAccount = account;
		when(accountRepo.findById(deletionAccount.getAccountId())).thenReturn(Optional.of(deletionAccount));
		
		//Act
		accountService.deleteById(deletionAccount.getAccountId());
		
		//Assert
		verify(accountRepo, times(1)).deleteById(deletionAccount.getAccountId());
	}
	
	@Test
	@DisplayName("Test for account deletion on non-existing account")
	public void testAccountDeletionByAccountIDTwo() {
		//Arrange
		long accountID = 111L;
		when(accountRepo.findById(accountID)).thenReturn(Optional.empty());
		
		//Act
		accountService.deleteById(accountID);
		
		//Assert
		verify(accountRepo, never()).deleteById(anyLong());
	}
	
	@Test
	@DisplayName("Test for successful find account by Account Number")
	public void testFindAccountByAccountNumberOne() {
		//Arrange
		Account expectedAccount = account;
		String accountNumber = expectedAccount.getAccountNumber();
		when(accountRepo.findByAccountNumber(accountNumber)).thenReturn(Optional.of(expectedAccount));
		
		//Act
		Account retrievedAccount = accountService.findAccountByAccountNumber(accountNumber);
		
		//Assert
		assertNotNull(retrievedAccount);
		assertEquals(expectedAccount, retrievedAccount);
	}
	
	@Test
	@DisplayName("Test for finding account for non-existing account number")
	public void testFindAccountByAccountNumberTwo() {
		//Arrange
		String accountNumber = "1234-5555-1111";
		when(accountRepo.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());
		
		//Act
		Account retrievedAccount = accountService.findAccountByAccountNumber(accountNumber);
		
		//Assert
		assertNull(retrievedAccount);
	}
	
	@Test
	@DisplayName("Test for normal withdrawal where withdrawal amount less than account balance")
	public void testWithdrawAccountOne() {
		//Arrange
		BigDecimal accountBalance = new BigDecimal("100.00");
		BigDecimal withdrawalAmount = new BigDecimal("50.00");
		
		//Act
		double result = accountService.withdrawAccountByAmount(accountBalance, withdrawalAmount);
		
		//Assert
		assertEquals(50.00, result, 0.001, "The balance after withdrawal should be correct!");
	}
	
	@Test
	@DisplayName("Test for normal withdrawal where withdrawal amount equal to account balance")
	public void testWithdrawAccountTwo() {
		//Arrange
		BigDecimal accountBalance = new BigDecimal("100.00");
		BigDecimal withdrawalAmount = new BigDecimal("100.00");
		
		//Act
		double result = accountService.withdrawAccountByAmount(accountBalance, withdrawalAmount);
		
		//Assert
		assertEquals(0, result, 0.001, "The balance after withdrawal should be correct!");
	}
	
	@Test
	@DisplayName("Test for normal withdrawal where withdrawal amount more than account balance")
	public void testWithdrawAccountThree() {
		//Arrange
		BigDecimal accountBalance = new BigDecimal("100.00");
		BigDecimal withdrawalAmount = new BigDecimal("150.00");
		
		//Act
		double result = accountService.withdrawAccountByAmount(accountBalance, withdrawalAmount);
		
		//Assert
		assertTrue(result < 0, "The balance after withdrawal should be correct!");
	}
	
	@Test
	@DisplayName("Test for Find Accounts By UserId when there's 2 accounts")
	public void testFindAllAccountsForSpecifiedUserOne() {
		//Arrange
		User currentUser = user;
		currentUser.setUserId(111L);
		currentUser.setAccounts(new Account());
		currentUser.setAccounts(new Account());
		List<Account> expectedAccounts = currentUser.getAccounts();
		when(accountRepo.findByAccountUserUserId(currentUser.getUserId())).thenReturn(expectedAccounts);
		
		//Act
		List<Account> actualAccounts = accountService.findAllAccountsByUserId(currentUser.getUserId());
		
		//Assert
		assertNotNull(actualAccounts);
		assertEquals(expectedAccounts, actualAccounts, "The returned accounts should match");
	}
	
	@Test
	@DisplayName("Test for Find All accounts when there is no bank account under user")
	public void testFindAllAccountsForSpecifiedUserTwo() {
		//Arrange
		User currentUser = user;
		currentUser.setUserId(111L);
		List<Account> expectedAccounts = currentUser.getAccounts();
		when(accountRepo.findByAccountUserUserId(currentUser.getUserId())).thenReturn(new ArrayList<>());
		
		//Act
		List<Account> actualAccounts = accountService.findAllAccountsByUserId(currentUser.getUserId());
		
		//Assert
		assertNotNull(actualAccounts);
		assertTrue(actualAccounts.isEmpty(), "The list should be empty.");
	}
	
	@Test
	@DisplayName("Test for retrieving a list of all accounts")
	public void testFindAllAccountsOne() {
		//Arrange
		List<Account> expectedAccounts = new ArrayList<>();
		expectedAccounts.add(new Account());
		expectedAccounts.add(new Account());
		when(accountRepo.findAll()).thenReturn(expectedAccounts);
		
		//Act
		List<Account> actualAccounts = accountService.getAllAccounts();
		
		//Assert
		assertNotNull(actualAccounts);
		assertEquals(expectedAccounts, actualAccounts, "The returned accounts should match");
	}
	
	@Test
	@DisplayName("Test for retrieving a list of empty accounts")
	public void testFindAllAccountsTo() {
		//Arrange
		List<Account> expectedAccounts = new ArrayList<>();
		when(accountRepo.findAll()).thenReturn(expectedAccounts);
		
		//Act
		List<Account> actualAccounts = accountService.getAllAccounts();
		
		//Assert
		assertNotNull(actualAccounts);
		assertTrue(actualAccounts.isEmpty(), "The returned list should have no accounts");
	}
	
}
