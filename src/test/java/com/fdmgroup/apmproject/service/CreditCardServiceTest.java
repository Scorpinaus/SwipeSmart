package com.fdmgroup.apmproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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

import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.Status;
import com.fdmgroup.apmproject.repository.CreditCardRepository;

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
