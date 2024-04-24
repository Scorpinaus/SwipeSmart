package com.fdmgroup.apmproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.core.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.MerchantCategoryCode;
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.repository.TransactionRepository;

/**
 * Tests the functionality of the StatusService class. This includes testing
 * methods related to status creation, update, deletion, and retrieval. The
 * tests verify the behavior of the service methods under various conditions,
 * such as when the status exists or not.
 *
 * @see StatusService The service class being tested.
 * @see StatusRepository The repository used by the service class.
 * @see Logger The logger used for logging within the service class.
 */
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

	@Mock
	private TransactionRepository transactionRepo;

	@Mock
	private Logger logger;

	@InjectMocks
	private TransactionService transactionService;

	private Transaction transactions;

	private CreditCard creditCard;
	private MerchantCategoryCode mcc;

	/**
	 * Sets up the test environment before each test method execution. Initializes a
	 * new status object with a status ID.
	 */
	@BeforeEach
	private void setUp() {
		transactions = new Transaction();
		transactions.setTransactionId(1L);
		creditCard = new CreditCard();
		mcc = new MerchantCategoryCode();
	}

	/**
	 * Tests the persist method of the TransactionService class for creating a new
	 * transaction. This test verifies that the persist method correctly saves a new
	 * transaction when it doesn't already exist in the database. It sets up a mock
	 * behavior for the transaction repository to return an empty optional when
	 * searching for the transaction by ID. Then, it invokes the persist method with
	 * the transaction object to be saved. Finally, it asserts that the transaction
	 * repository's save method is called with the correct transaction object.
	 *
	 * @see TransactionService The service class being tested.
	 * @see TransactionRepository The repository used by the service class.
	 */
	@Test
	@DisplayName("Test Persist function for new Transaction")
	public void testPersistOne() {
		// Arrange
		Transaction transactionOne = transactions;
		when(transactionRepo.findById(transactionOne.getTransactionId())).thenReturn(Optional.empty());

		// Act
		transactionService.persist(transactionOne);

		// Assert
		verify(transactionRepo).save(transactionOne);
	}

	/**
	 * Tests the persist method of the TransactionService class for updating an
	 * existing transaction. This test verifies that the persist method doesn't save
	 * the transaction again if it already exists in the database. It sets up a mock
	 * behavior for the transaction repository to return an optional containing the
	 * transaction when searching for it by ID. Then, it invokes the persist method
	 * with the transaction object. Finally, it asserts that the transaction
	 * repository's save method is never called and there are no more interactions
	 * with the repository.
	 *
	 * @see TransactionService The service class being tested.
	 * @see TransactionRepository The repository used by the service class.
	 */
	@Test
	@DisplayName("Test Persist function for existing Transaction")
	public void testPersistTwo() {
		// Arrange
		Transaction transactionOne = transactions;
		when(transactionRepo.findById(transactionOne.getTransactionId())).thenReturn(Optional.of(transactionOne));

		// Act
		transactionService.persist(transactionOne);

		// Assert
		verify(transactionRepo, never()).save(transactionOne);
		verifyNoMoreInteractions(transactionRepo);
	}

	/**
	 * Tests the update method of the TransactionService class for a non-existing
	 * transaction. This test verifies that the update method doesn't save the
	 * transaction if it doesn't exist in the database. It sets up a mock behavior
	 * for the transaction repository to return an empty optional when searching for
	 * the transaction by ID. Then, it invokes the update method with the
	 * transaction object. Finally, it asserts that the transaction repository's
	 * save method is never called and there are no more interactions with the
	 * repository.
	 *
	 * @see TransactionService The service class being tested.
	 * @see TransactionRepository The repository used by the service class.
	 */
	@Test
	@DisplayName("Test update function for non-existing Transaction")
	public void testUpdateOne() {
		// Arrange
		Transaction transactionOne = transactions;
		when(transactionRepo.findById(transactionOne.getTransactionId())).thenReturn(Optional.empty());

		// Act
		transactionService.update(transactionOne);

		// Assert
		verify(transactionRepo, never()).save(transactionOne);
		verifyNoMoreInteractions(transactionRepo);
	}

	/**
	 * Tests the update method of the TransactionService class for an existing
	 * transaction. This test verifies that the update method saves the transaction
	 * if it already exists in the database. It sets up a mock behavior for the
	 * transaction repository to return an optional containing the transaction when
	 * searching for it by ID. Then, it invokes the update method with the
	 * transaction object. Finally, it asserts that the transaction repository's
	 * save method is called with the transaction object and there are no more
	 * interactions with the repository.
	 *
	 * @param None
	 * @return None
	 * @throws None
	 * @see TransactionService The service class being tested.
	 * @see TransactionRepository The repository used by the service class.
	 */
	@Test
	@DisplayName("Test update function for existing Transaction")
	public void testUpdateTwo() {
		// Arrange
		Transaction transactionOne = transactions;
		when(transactionRepo.findById(transactionOne.getTransactionId())).thenReturn(Optional.of(transactionOne));

		// Act
		transactionService.update(transactionOne);

		// Assert
		verify(transactionRepo).save(transactionOne);
		verifyNoMoreInteractions(transactionRepo);
	}

	/**
	 * Tests the findById method of the TransactionService class for a non-existing
	 * transaction. This test ensures that the findById method returns null when the
	 * transaction does not exist in the database. It sets up a mock behavior for
	 * the transaction repository to return an empty optional when searching for the
	 * transaction by ID. Then, it invokes the findById method with the transaction
	 * ID. Finally, it asserts that the result is null and there are no more
	 * interactions with the transaction repository.
	 *
	 * @param None
	 * @return None
	 * @throws None
	 * @see TransactionService The service class being tested.
	 * @see TransactionRepository The repository used by the service class.
	 */
	@Test
	@DisplayName("Test findById function for non-existing Transaction")
	public void testFindByIdOne() {
		// Arrange
		Transaction transactionOne = transactions;
		when(transactionRepo.findById(transactionOne.getTransactionId())).thenReturn(Optional.empty());

		// Act
		Transaction result = transactionService.findById(transactionOne.getTransactionId());

		// Assert
		assertNull(result);
		verifyNoMoreInteractions(transactionRepo);
	}

	/**
	 * Tests the findById method of the TransactionService class for an existing
	 * transaction. This test verifies that the findById method returns the correct
	 * transaction when it exists in the database. It sets up a mock behavior for
	 * the transaction repository to return an optional containing the transaction
	 * when searching by ID. Then, it calls the findById method with the transaction
	 * ID. Finally, it asserts that the result is not null, matches the expected
	 * transaction, and there are no further interactions with the transaction
	 * repository.
	 *
	 * @param None
	 * @return None
	 * @throws None
	 * @see TransactionService The service class being tested.
	 * @see TransactionRepository The repository used by the service class.
	 */
	@Test
	@DisplayName("Test findById function for existing Transaction")
	public void testFindByIdTwo() {
		// Arrange
		Transaction transactionOne = transactions;
		when(transactionRepo.findById(transactionOne.getTransactionId())).thenReturn(Optional.of(transactionOne));

		// Act
		Transaction result = transactionService.findById(transactionOne.getTransactionId());

		// Assert
		assertNotNull(result);
		assertEquals(result, transactionOne);
		verifyNoMoreInteractions(transactionRepo);
	}

	/**
	 * Tests the deleteById method of the TransactionService class for a
	 * non-existing transaction. This test ensures that the deleteById method does
	 * not attempt to delete a transaction that does not exist in the database. It
	 * sets up a mock behavior for the transaction repository to return an empty
	 * optional when searching by ID. Then, it calls the deleteById method with the
	 * transaction ID. Finally, it verifies that the transaction repository's
	 * deleteById method was not invoked and there are no further interactions with
	 * it.
	 *
	 * @param None
	 * @return None
	 * @throws None
	 * @see TransactionService The service class being tested.
	 * @see TransactionRepository The repository used by the service class.
	 */
	@Test
	@DisplayName("Test deleteById function for non-existing Transaction")
	public void testDeleteByIdOne() {
		// Arrange
		Transaction transactionOne = transactions;
		when(transactionRepo.findById(transactionOne.getTransactionId())).thenReturn(Optional.empty());

		// Act
		transactionService.deleteById(transactionOne.getTransactionId());

		// Assert
		verify(transactionRepo, never()).deleteById(transactionOne.getTransactionId());
		verifyNoMoreInteractions(transactionRepo);
	}

	/**
	 * Verifies the deletion of a transaction by its ID in the transaction
	 * repository.
	 * <p>
	 * This test method checks that the `deleteById` method of the
	 * `TransactionService` class is called correctly and that it interacts with the
	 * repository as expected. It sets up a predefined transaction, mocks the
	 * retrieval of this transaction by ID using the repository, and then tests the
	 * delete functionality. The method ensures that the repository's deleteById
	 * method is called once and that no more interactions occur with the repository
	 * after this operation.
	 *
	 * @param None There are no parameters for this test method.
	 * @return None This method does not return a value as it is a test method.
	 * @throws None This method does not throw any exceptions.
	 * @see TransactionService#deleteById(Long) For the service method being tested
	 *      which handles the actual deletion process.
	 */

	@Test
	@DisplayName("Test deleteById function for existing Transaction")
	public void testDeleteByIdTwo() {
		// Arrange
		Transaction transactionOne = transactions;
		when(transactionRepo.findById(transactionOne.getTransactionId())).thenReturn(Optional.of(transactionOne));

		// Act
		transactionService.deleteById(transactionOne.getTransactionId());

		// Assert
		verify(transactionRepo).deleteById(transactionOne.getTransactionId());
		verifyNoMoreInteractions(transactionRepo);
	}

}
