package com.fdmgroup.apmproject.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.apmproject.model.MerchantCategoryCode;
import com.fdmgroup.apmproject.repository.MerchantCategoryCodeRepository;
import com.fdmgroup.apmproject.service.MerchantCategoryCodeService;

/**
 * Unit tests for the MerchantCategoryCodeService class. This class contains
 * tests to verify the functionality of the MerchantCategoryCodeService,
 * particularly its interaction with the MerchantCategoryCodeRepository.
 * 
 * @see MerchantCategoryCodeService The class being tested.
 * @see MerchantCategoryCodeRepository The repository being mocked.
 */
@SpringBootTest
class MerchantCategoryCodeServiceTest {

	private MerchantCategoryCodeService merchantCategoryCodeService;

	@Mock
	private MerchantCategoryCodeRepository mockRepo;

	@BeforeEach
	void setUp() {
		merchantCategoryCodeService = new MerchantCategoryCodeService(mockRepo);
	}

	/**
	 * Tests the persist method of the MerchantCategoryCodeService class. This
	 * method verifies whether the persist method correctly saves a new
	 * MerchantCategoryCode object.
	 *
	 * @param merchantCategoryCode The MerchantCategoryCode object to be persisted.
	 * @throws ExceptionType If an error occurs during the test execution.
	 * @see MerchantCategoryCodeService The class containing the persist method.
	 * @see MerchantCategoryCodeRepository The repository used for persisting.
	 */
	@Test
	@DisplayName("Test for persist method")
	void test1() {
		// Arrange
		MerchantCategoryCode merchantCategoryCode = new MerchantCategoryCode(12, "Trial Category");
		merchantCategoryCode.setMerchantCategoryCodeId(12);

		// Act
		merchantCategoryCodeService.persist(merchantCategoryCode);

		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(merchantCategoryCode.getMerchantCategoryCodeId());
		order.verify(mockRepo).save(merchantCategoryCode);
		order.verifyNoMoreInteractions();
	}

	/**
	 * Tests the update method of the MerchantCategoryCodeService class when the
	 * MerchantCategoryCode exists in the database. This method verifies whether the
	 * update method correctly updates an existing MerchantCategoryCode object.
	 *
	 * @param expectedMerchantCategoryCode The MerchantCategoryCode object to be
	 *                                     updated.
	 * @return No return value.
	 * @throws ExceptionType If an error occurs during the test execution.
	 * @see MerchantCategoryCodeService The class containing the update method.
	 * @see MerchantCategoryCodeRepository The repository used for database
	 *      operations.
	 */
	@Test
	@DisplayName("Test for update method when MerchantCategoryCode exists in database")
	void test2() {
		// Arrange
		MerchantCategoryCode expectedMerchantCategoryCode = new MerchantCategoryCode(12, "Trial Category");
		expectedMerchantCategoryCode.setMerchantCategoryCodeId(12);
		Optional<MerchantCategoryCode> tempMerchantCategoryCode = Optional.ofNullable(expectedMerchantCategoryCode);
		when(mockRepo.findById(expectedMerchantCategoryCode.getMerchantCategoryCodeId()))
				.thenReturn(tempMerchantCategoryCode);

		// Act
		merchantCategoryCodeService.update(expectedMerchantCategoryCode);

		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(expectedMerchantCategoryCode.getMerchantCategoryCodeId());
		order.verify(mockRepo).save(expectedMerchantCategoryCode);
		order.verifyNoMoreInteractions();
	}

	/**
	 * Tests the update method of the MerchantCategoryCodeService class when the
	 * MerchantCategoryCode exists in the database. This method verifies whether the
	 * update method correctly updates an existing MerchantCategoryCode object.
	 *
	 * @param expectedMerchantCategoryCode The MerchantCategoryCode object to be
	 *                                     updated.
	 * @return No return value.
	 * @throws ExceptionType If an error occurs during the test execution.
	 * @see MerchantCategoryCodeService The class containing the update method.
	 * @see MerchantCategoryCodeRepository The repository used for database
	 *      operations.
	 */
	@Test
	@DisplayName("Test for update method when MerchantCategoryCode does not exist in database")
	void test3() {
		// Arrange
		MerchantCategoryCode expectedMerchantCategoryCode = new MerchantCategoryCode(12, "Trial Category");
		expectedMerchantCategoryCode.setMerchantCategoryCodeId(12);

		// Act
		merchantCategoryCodeService.update(expectedMerchantCategoryCode);

		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(expectedMerchantCategoryCode.getMerchantCategoryCodeId());
		order.verifyNoMoreInteractions();
		verify(mockRepo, never()).save(expectedMerchantCategoryCode);
	}

	/**
	 * Tests the findById method of the MerchantCategoryCodeService class when the
	 * MerchantCategoryCode exists in the database. This method verifies whether the
	 * findById method correctly retrieves an existing MerchantCategoryCode object.
	 *
	 * @param expectedMerchantCategoryCode The expected MerchantCategoryCode object
	 *                                     with the specified ID.
	 * @return Description of the retrieved MerchantCategoryCode object.
	 * @throws ExceptionType If an error occurs during the test execution.
	 * @see MerchantCategoryCodeService The class containing the findById method.
	 * @see MerchantCategoryCodeRepository The repository used for database
	 *      operations.
	 */
	@Test
	@DisplayName("Test for findById method when MerchantCategoryCode exists in database")
	void test4() {
		// Arrange
		MerchantCategoryCode expectedMerchantCategoryCode = new MerchantCategoryCode(12, "Trial Category");
		expectedMerchantCategoryCode.setMerchantCategoryCodeId(12);
		Optional<MerchantCategoryCode> tempMerchantCategoryCode = Optional.ofNullable(expectedMerchantCategoryCode);
		when(mockRepo.findById(expectedMerchantCategoryCode.getMerchantCategoryCodeId()))
				.thenReturn(tempMerchantCategoryCode);

		// Act
		MerchantCategoryCode actualMerchantCategoryCode = merchantCategoryCodeService
				.findById(expectedMerchantCategoryCode.getMerchantCategoryCodeId());

		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(expectedMerchantCategoryCode.getMerchantCategoryCodeId());
		order.verifyNoMoreInteractions();
		assertEquals(expectedMerchantCategoryCode, actualMerchantCategoryCode);
	}

	/**
	 * Tests the findById method of the MerchantCategoryCodeService class when the
	 * MerchantCategoryCode does not exist in the database. This method verifies
	 * whether the findById method returns null when no MerchantCategoryCode is
	 * found with the specified ID.
	 *
	 * @param id The ID of the MerchantCategoryCode being searched for.
	 * @return Description of the retrieved MerchantCategoryCode object, which is
	 *         expected to be null.
	 * @throws ExceptionType If an error occurs during the test execution.
	 * @see MerchantCategoryCodeService The class containing the findById method.
	 * @see MerchantCategoryCodeRepository The repository used for database
	 *      operations.
	 */
	@Test
	@DisplayName("Test for findById method when MerchantCategoryCode does not exist in database")
	void test5() {
		// Arrange
		MerchantCategoryCode expectedMerchantCategoryCode = null;

		// Act
		MerchantCategoryCode actualMerchantCategoryCode = merchantCategoryCodeService.findById(2);

		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(2);
		order.verifyNoMoreInteractions();
		assertEquals(expectedMerchantCategoryCode, actualMerchantCategoryCode);
	}

	/**
	 * Tests the findByMerchantCategory method of the MerchantCategoryCodeService
	 * class when the MerchantCategoryCode exists in the database. This method
	 * verifies whether the findByMerchantCategory method returns the expected
	 * MerchantCategoryCode object when it exists in the database.
	 *
	 * @param merchantCategory The merchant category of the MerchantCategoryCode
	 *                         being searched for.
	 * @return The retrieved MerchantCategoryCode object, which is expected to match
	 *         the specified merchant category.
	 * @throws ExceptionType If an error occurs during the test execution.
	 * @see MerchantCategoryCodeService The class containing the
	 *      findByMerchantCategory method.
	 * @see MerchantCategoryCodeRepository The repository used for database
	 *      operations.
	 */
	@Test
	@DisplayName("Test for findByMerchantCategory method when MerchantCategoryCode exists in database")
	void test6() {
		// Arrange
		MerchantCategoryCode expectedMerchantCategoryCode = new MerchantCategoryCode(12, "Trial Category");
		Optional<MerchantCategoryCode> tempMerchantCategoryCode = Optional.ofNullable(expectedMerchantCategoryCode);
		when(mockRepo.findByMerchantCategory(expectedMerchantCategoryCode.getMerchantCategory()))
				.thenReturn(tempMerchantCategoryCode);
		// Act
		MerchantCategoryCode actualMerchantCategoryCode = merchantCategoryCodeService
				.findByMerchantCategory(expectedMerchantCategoryCode.getMerchantCategory());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByMerchantCategory(expectedMerchantCategoryCode.getMerchantCategory());
		order.verifyNoMoreInteractions();
		assertEquals(expectedMerchantCategoryCode, actualMerchantCategoryCode);
	}

	/**
	 * Tests the findByMerchantCategory method of the MerchantCategoryCodeService
	 * class when the MerchantCategoryCode does not exist in the database. This
	 * method verifies whether the findByMerchantCategory method returns null when
	 * no MerchantCategoryCode is found in the database for the specified merchant
	 * category.
	 *
	 * @param merchantCategory The merchant category of the MerchantCategoryCode
	 *                         being searched for.
	 * @return The retrieved MerchantCategoryCode object, which is expected to be
	 *         null if not found in the database.
	 * @throws ExceptionType If an error occurs during the test execution.
	 * @see MerchantCategoryCodeService The class containing the
	 *      findByMerchantCategory method.
	 * @see MerchantCategoryCodeRepository The repository used for database
	 *      operations.
	 */
	@Test
	@DisplayName("Test for findByMerchantCategory method when MerchantCategoryCode does not exist in database")
	void test7() {
		// Arrange
		MerchantCategoryCode expectedMerchantCategoryCode = null;

		// Act
		MerchantCategoryCode actualMerchantCategoryCode = merchantCategoryCodeService.findByMerchantCategory("test");

		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByMerchantCategory("test");
		order.verifyNoMoreInteractions();
		assertEquals(expectedMerchantCategoryCode, actualMerchantCategoryCode);
	}

	/**
	 * Tests the deleteById method of the MerchantCategoryCodeService class when the
	 * MerchantCategoryCode exists in the database. This method verifies whether the
	 * deleteById method correctly deletes the MerchantCategoryCode with the
	 * specified ID from the database.
	 *
	 * @param id The ID of the MerchantCategoryCode to be deleted.
	 * @throws ExceptionType If an error occurs during the test execution.
	 * @see MerchantCategoryCodeService The class containing the deleteById method.
	 * @see MerchantCategoryCodeRepository The repository used for database
	 *      operations.
	 */
	@Test
	@DisplayName("Test for deleteById method when MerchantCategoryCode exists in database")
	void test8() {
		// Arrange
		MerchantCategoryCode expectedMerchantCategoryCode = new MerchantCategoryCode(12, "Trial Category");
		expectedMerchantCategoryCode.setMerchantCategoryCodeId(12);
		Optional<MerchantCategoryCode> tempMerchantCategoryCode = Optional.ofNullable(expectedMerchantCategoryCode);
		when(mockRepo.findById(expectedMerchantCategoryCode.getMerchantCategoryCodeId()))
				.thenReturn(tempMerchantCategoryCode);
		// Act
		merchantCategoryCodeService.deleteById(expectedMerchantCategoryCode.getMerchantCategoryCodeId());

		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(expectedMerchantCategoryCode.getMerchantCategoryCodeId());
		order.verify(mockRepo).deleteById(expectedMerchantCategoryCode.getMerchantCategoryCodeId());
		order.verifyNoMoreInteractions();

	}

	/**
	 * Tests the deleteById method of the MerchantCategoryCodeService class when the
	 * MerchantCategoryCode does not exist in the database. This method ensures that
	 * the deleteById method does not delete anything from the database when the
	 * specified MerchantCategoryCode does not exist.
	 *
	 * @param id The ID of the MerchantCategoryCode to be deleted.
	 * @throws ExceptionType If an error occurs during the test execution.
	 * @see MerchantCategoryCodeService The class containing the deleteById method.
	 * @see MerchantCategoryCodeRepository The repository used for database
	 *      operations.
	 */
	@Test
	@DisplayName("Test for deleteById method when MerchantCategoryCode does not exist in database")
	void test9() {
		// Arrange
		MerchantCategoryCode expectedMerchantCategoryCode = new MerchantCategoryCode();
		expectedMerchantCategoryCode.setMerchantCategoryCodeId(9);

		// Act
		merchantCategoryCodeService.deleteById(expectedMerchantCategoryCode.getMerchantCategoryCodeId());

		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(expectedMerchantCategoryCode.getMerchantCategoryCodeId());
		order.verifyNoMoreInteractions();
		verify(mockRepo, never()).deleteById(expectedMerchantCategoryCode.getMerchantCategoryCodeId());
	}

}
