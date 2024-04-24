package com.fdmgroup.apmproject.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link CreditCard} for unit testing of credit card entities.
 *
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */

public class CreditCardTest {

	// Global Variable
	User user;
	CreditCard creditCard;
	Status status;
	CreditCard creditCard1;

	@BeforeEach
	public void setUp() {
		user = new User("jackytan", "Qwerty1", "Sentosa", "Jacky", "Tan");
		status = new Status("Approved");
		String creditCardNumber = "1234-5678-1234-5678";
		String pin = "123";
		creditCard = new CreditCard(creditCardNumber, pin, 3000, "Ultimate Cashback Card", status, 0, user);
	}

	
	/**
	 * Tests whether the instantiation of the CreditCard class results in the same instance.
	 * Compares a CreditCard instance with itself to check for equality.
	 *
	 * @return True if the CreditCard instance is equal to itself, otherwise false.
	 * @see CreditCard#equals(Object)
	 */
	@Test
	@DisplayName("The intansiation of CreditCard class is of the same instance")
    public void testEqualsSameInstance() {
        assertTrue(creditCard.equals(creditCard));
    }
	
	/**
	 * Tests whether the instantiation of the CreditCard class results in the same attribute values.
	 * Compares the attributes of a CreditCard instance with the expected values.
	 *
	 * @return True if the attributes of the CreditCard instance match the expected values, otherwise false.
	 * @see CreditCard#getCardType()
	 * @see CreditCard#getCardLimit()
	 * @see CreditCard#getPin()
	 * @see CreditCard#getCreditCardUser()
	 * @see CreditCard#getCreditCardStatus()
	 * @see CreditCard#getAmountUsed()
	 */
	@Test
	@DisplayName("The intansiation of CreditCard class gives the same attribute values")
	public void testCreditCardValue() {
		// arrange
		String expectedType = "Ultimate Cashback Card";
		double expectedLimit = 3000;
		String expectedPin = "123";
		User expectedUser = user;
		Status expectedStatus = status;
		double expectedAmountUsed = 0;

		// act
		String resultType = creditCard.getCardType();
		double resultLimit = creditCard.getCardLimit();
		String resultPin = creditCard.getPin();
		User resultUser = creditCard.getCreditCardUser();
		Status resultStatus = creditCard.getCreditCardStatus();
		double resultAmountUsed = creditCard.getAmountUsed();

		// assert
		assertEquals(expectedType, resultType);
		assertEquals(expectedLimit, resultLimit);
		assertEquals(expectedPin, resultPin);
		assertEquals(expectedUser, resultUser);
		assertEquals(expectedStatus, resultStatus);
		assertEquals(expectedAmountUsed, resultAmountUsed);
	}

	
	/**
	 * Tests whether two CreditCard objects instantiated with the same arguments are equal to each other.
	 * Compares the equality of two CreditCard instances with identical attribute values.
	 *
	 * @return True if the two CreditCard instances are equal, otherwise false.
	 * @see CreditCard#getCardNumber()
	 * @see CreditCard#getPin()
	 * @see CreditCard#getCardLimit()
	 * @see CreditCard#getCardType()
	 * @see CreditCard#getCreditCardStatus()
	 * @see CreditCard#getAmountUsed()
	 * @see CreditCard#getCreditCardUser()
	 */
	@Test
	@DisplayName("Two objects instansiated with the same argument are equals to each other")
	public void testEqualsSameFields() {
		creditCard1 = new CreditCard("1234-5678-1234-5678", "123", 3000, "Ultimate Cashback Card", status, 0, user);
		assertTrue(creditCard.equals(creditCard1));
	}

	/**
	 * Tests whether two CreditCard objects instantiated with different arguments are different from each other.
	 * Compares the equality of two CreditCard instances with different attribute values.
	 *
	 * @return True if the two CreditCard instances are not equal, otherwise false.
	 * @see CreditCard#getCardNumber()
	 * @see CreditCard#getPin()
	 * @see CreditCard#getCardLimit()
	 * @see CreditCard#getCardType()
	 * @see CreditCard#getCreditCardStatus()
	 * @see CreditCard#getAmountUsed()
	 * @see CreditCard#getCreditCardUser()
	 */
    @Test
    @DisplayName("Two objects instansiated with different argument are different to each other")
    public void testEquals_DifferentFields() {
    	creditCard1 = new CreditCard("1235-5678-1234-5378", "125", 2000, "Ultimate Cashback Card", status, 0, user);
        assertFalse(creditCard.equals(creditCard1));
    } 
    
    /**
     * Tests the functionality of the addTransaction method by verifying if the amount used increases
     * when a transaction is added.
     *
     * @return Description of what the return value represents or how it should be used.
     * @throws ExceptionType If the method encounters an unexpected condition.
     * @see CreditCard#addTransaction(double)
     * @see CreditCard#getAmountUsed()
     */
    @Test
    @DisplayName("addTransaction function increases amount used when trasaction is added")
    public void testAddTransaction() {
    	double expectedAmountUsed = 100;
    	creditCard.addTransaction(100);
    	double resultAmountUsed = creditCard.getAmountUsed();
    	
    	assertEquals(expectedAmountUsed, resultAmountUsed);
    }
}
