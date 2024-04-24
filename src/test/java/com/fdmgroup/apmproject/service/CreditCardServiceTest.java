package com.fdmgroup.apmproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.core.Logger;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.Status;
import com.fdmgroup.apmproject.repository.CreditCardRepository;

/**
 * Unit tests for the CreditCardService class. This class utilizes
 * MockitoExtension for mocking dependencies such as the CreditCardRepository
 * and Logger. It tests various functionalities of the CreditCardService class.
 *
 * @throws ExceptionType If any unexpected conditions occur during testing.
 * @see CreditCardService
 * @see CreditCardRepository
 * @see Logger
 */

@ExtendWith(MockitoExtension.class)
public class CreditCardServiceTest {

	@Mock
	private CreditCardRepository creditCardRepo;

	@Mock
	private Logger logger;

	@InjectMocks
	private CreditCardService creditCardService;

	private CreditCard card;

	@BeforeEach
	public void setUp() {
		card = new CreditCard();
		String creditCardNumber = "1234-5678-1234-5678";
		card.setCreditCardNumber(creditCardNumber);
		card.setCreditCardId(1L);
	}

	/**
	 * Tests the persistence of a new credit card in the CreditCardService class.
	 * This method checks if a new credit card is correctly persisted by the
	 * service.
	 *
	 * @throws ExceptionType If any unexpected conditions occur during testing.
	 * @see CreditCardService#persist(CreditCard)
	 * @see CreditCardRepository
	 */
	@Test
	@DisplayName("1. Persist test for new credit card")
	void testPersistOne() {
		// Arrange
		CreditCard newCard = card;
		when(creditCardRepo.findById(newCard.getCreditCardId())).thenReturn(Optional.empty());

		// Act
		creditCardService.persist(newCard);

		// Assert
		verify(creditCardRepo, times(1)).save(newCard);
	}

	/**
	 * Tests the persistence of an existing credit card in the CreditCardService
	 * class. This method verifies that an existing credit card is not re-saved by
	 * the service.
	 *
	 * @throws ExceptionType If any unexpected conditions occur during testing.
	 * @see CreditCardService#persist(CreditCard)
	 * @see CreditCardRepository
	 */
	@Test
	@DisplayName("2. Persist test for existing credit card")
	void testPersistTwo() {
		// Arrange
		CreditCard existingCard = card;
		when(creditCardRepo.findById(existingCard.getCreditCardId())).thenReturn(Optional.of(existingCard));

		// Act
		creditCardService.persist(existingCard);

		// Assert
		verify(creditCardRepo, never()).save(existingCard);
	}

	/**
	 * Tests the update operation for a new credit card in the CreditCardService
	 * class. This method verifies that when a new credit card is passed for update,
	 * it is not saved as it doesn't exist in the repository.
	 *
	 * @throws ExceptionType If any unexpected conditions occur during testing.
	 * @see CreditCardService#update(CreditCard)
	 * @see CreditCardRepository
	 */
	@Test
	@DisplayName("3. Update test for new credit card")
	void testUpdateOne() {
		// Arrange
		CreditCard newCard = card;
		when(creditCardRepo.findById(newCard.getCreditCardId())).thenReturn(Optional.empty());

		// Act
		creditCardService.update(newCard);

		// Assert
		verify(creditCardRepo, never()).save(newCard);
	}

	/**
	 * Tests the update operation for an existing credit card in the
	 * CreditCardService class. This method verifies that when an existing credit
	 * card is passed for update, it is saved in the repository.
	 *
	 * @throws ExceptionType If any unexpected conditions occur during testing.
	 * @see CreditCardService#update(CreditCard)
	 * @see CreditCardRepository
	 */
	@Test
	@DisplayName("4. Update test for existing credit card")
	void testUpdateTwo() {
		// Arrange
		CreditCard existingCard = card;
		when(creditCardRepo.findById(existingCard.getCreditCardId())).thenReturn(Optional.of(existingCard));

		// Act
		creditCardService.update(existingCard);

		// Assert
		verify(creditCardRepo).save(existingCard);
	}

	/**
	 * Tests the findById method in the CreditCardService class when no credit card
	 * is found. This method verifies that when the findById method is called with a
	 * credit card ID and no matching card is found in the repository, it returns
	 * null.
	 *
	 * @throws ExceptionType If any unexpected conditions occur during testing.
	 * @see CreditCardService#findById(Long)
	 * @see CreditCardRepository
	 */
	@Test
	@DisplayName("5. FindById test when no credit card is found")
	void testFindByIdOne() {
		// Arrange
		CreditCard cardOne = card;
		when(creditCardRepo.findById(cardOne.getCreditCardId())).thenReturn(Optional.empty());

		// Act
		CreditCard result = creditCardService.findById(cardOne.getCreditCardId());

		// Assert
		assertNull(result);
	}

	/**
	 * Tests the findById method in the CreditCardService class when an existing
	 * credit card is found. This method verifies that when the findById method is
	 * called with a credit card ID and a matching card is found in the repository,
	 * it returns the correct credit card object.
	 *
	 * @throws ExceptionType If any unexpected conditions occur during testing.
	 * @see CreditCardService#findById(Long)
	 * @see CreditCardRepository
	 */
	@Test
	@DisplayName("6. FindById test when existing credit card found")
	void testFindByIdTwo() {
		// Arrange
		CreditCard cardOne = card;
		when(creditCardRepo.findById(cardOne.getCreditCardId())).thenReturn(Optional.of(cardOne));

		// Act
		CreditCard result = creditCardService.findById(cardOne.getCreditCardId());

		// Assert
		assertNotNull(result);
		assertEquals(cardOne, result);
	}

	/**
	 * Tests the deleteById method in the CreditCardService class for a non-existing
	 * card. This method verifies that when the deleteById method is called with a
	 * credit card ID and no matching card is found in the repository, it does not
	 * perform any deletion operation.
	 *
	 * @throws ExceptionType If any unexpected conditions occur during testing.
	 * @see CreditCardService#deleteById(Long)
	 * @see CreditCardRepository
	 */
	@Test
	@DisplayName("7. deteleById test when for non-existing card")
	void testDeleteByIdOne() {
		// Arrange
		CreditCard cardOne = card;
		when(creditCardRepo.findById(cardOne.getCreditCardId())).thenReturn(Optional.empty());

		// Act
		creditCardService.deleteById(cardOne.getCreditCardId());

		// Assert
		verify(creditCardRepo, never()).deleteById(cardOne.getCreditCardId());
	}

	/**
	 * Tests the deleteById method in the CreditCardService class for an existing
	 * credit card. This method verifies that when the deleteById method is called
	 * with a valid credit card ID, and a matching card is found in the repository,
	 * it performs the deletion operation.
	 *
	 * @throws ExceptionType If any unexpected conditions occur during testing.
	 * @see CreditCardService#deleteById(Long)
	 * @see CreditCardRepository
	 */
	@Test
	@DisplayName("8. deleteById test for existing credit card")
	void testDeleteByIdTwo() {
		// Arrange
		CreditCard cardOne = card;
		when(creditCardRepo.findById(cardOne.getCreditCardId())).thenReturn(Optional.of(cardOne));
		// Act
		creditCardService.deleteById(cardOne.getCreditCardId());

		// Assert
		verify(creditCardRepo).deleteById(cardOne.getCreditCardId());
	}

	/**
	 * Tests the findByCreditCardNumber method in the CreditCardService class for a
	 * non-existing credit card. This method verifies that when the
	 * findByCreditCardNumber method is called with a credit card number that does
	 * not exist in the repository, it returns null.
	 *
	 * @throws ExceptionType If any unexpected conditions occur during testing.
	 * @see CreditCardService#findByCreditCardNumber(String)
	 * @see CreditCardRepository
	 */
	@Test
	@DisplayName("9. FindByCreditCardNumber test for non-existing credit card")
	void testFindByNumberOne() {
		// Arrange
		CreditCard cardOne = card;
		when(creditCardRepo.findByCreditCardNumber(cardOne.getCreditCardNumber())).thenReturn(Optional.empty());

		// Act
		CreditCard result = creditCardService.findByCreditCardNumber(cardOne.getCreditCardNumber());

		// Assert
		assertNull(result);
	}

	/**
	 * Tests the findByCreditCardNumber method in the CreditCardService class for an
	 * existing credit card. This method verifies that when the
	 * findByCreditCardNumber method is called with a credit card number that exists
	 * in the repository, it returns the corresponding credit card.
	 *
	 * @throws ExceptionType If any unexpected conditions occur during testing.
	 * @see CreditCardService#findByCreditCardNumber(String)
	 * @see CreditCardRepository
	 */
	@Test
	@DisplayName("10. FindByCreditCardNumber test for existing credit card")
	void testFindByNumberTwo() {
		// Arrange
		CreditCard cardOne = card;
		when(creditCardRepo.findByCreditCardNumber(cardOne.getCreditCardNumber())).thenReturn(Optional.of(cardOne));

		// Act
		CreditCard result = creditCardService.findByCreditCardNumber(cardOne.getCreditCardNumber());

		// Assert
		assertNotNull(result);
		assertEquals(cardOne, result);
	}

	/**
	 * Tests the findCreditCardsByStatus method in the CreditCardService class to
	 * retrieve all credit cards with a pending status. This method ensures that
	 * when the findCreditCardsByStatus method is invoked with a status indicating
	 * pending credit cards, it returns a list containing all credit cards with that
	 * status.
	 *
	 * @throws ExceptionType If any unexpected conditions occur during testing.
	 * @see CreditCardService#findCreditCardsByStatus(Status)
	 * @see CreditCardRepository
	 */
	@Test
	@DisplayName("11. FindByCreditCardStatus test to return all credit card with pending status")
	void testCreditCardStatusOne() {
		// Arrange
		Status statusName = new Status();
		statusName.setStatusName("Approved");
		CreditCard cardOne = card;
		cardOne.setCreditCardStatus(statusName);
		List<CreditCard> expected = new ArrayList<>();
		expected.add(cardOne);
		when(creditCardRepo.findByCreditCardStatus(statusName)).thenReturn(expected);

		// Act
		List<CreditCard> result = creditCardService.findCreditCardsByStatus(statusName);

		// Assert
		assertNotNull(result);
		assertEquals(expected.size(), result.size());
		assertEquals(expected, result);
	}

	/**
	 * Tests the findAllCreditCards method in the CreditCardService class to
	 * retrieve all credit cards. This method ensures that when the
	 * findAllCreditCards method is invoked, it returns a list containing all
	 * available credit cards.
	 *
	 * @throws ExceptionType If any unexpected conditions occur during testing.
	 * @see CreditCardService#findAllCreditCards()
	 * @see CreditCardRepository
	 */
	@Test
	@DisplayName("11. FindAllCreditCards test to return all credit card")
	void testAllCreditCards() {
		// Arrange
		CreditCard cardOne = card;
		CreditCard cardTwo = card;
		List<CreditCard> expected = new ArrayList<>();
		expected.add(cardOne);
		expected.add(cardTwo);
		when(creditCardRepo.findAll()).thenReturn(expected);

		// Act
		List<CreditCard> actual = creditCardService.findAllCreditCards();

		// Assert
		assertEquals(expected, actual, "Actual list should match expected");
	}
}
