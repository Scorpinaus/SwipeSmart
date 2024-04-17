package com.fdmgroup.apmproject.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CreditCardTest {
	
	//Global Variable 
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
	
	@Test
	@DisplayName("The intansiation of CreditCard class is of the same instance")
    public void testEqualsSameInstance() {
        assertTrue(creditCard.equals(creditCard));
    }
	
	@Test
	@DisplayName("The intansiation of CreditCard class gives the same attribute values")
    public void testCreditCardValue() {
		//arrange
		String expectedType = "Ultimate Cashback Card";
		double expectedLimit = 3000;
		String expectedPin = "123";
		User expectedUser = user;
		Status expectedStatus = status;
		double expectedAmountUsed = 0;
		
		//act
		String resultType = creditCard.getCardType();
		double resultLimit = creditCard.getCardLimit();
		String resultPin = creditCard.getPin();
		User resultUser = creditCard.getCreditCardUser();
		Status resultStatus = creditCard.getCreditCardStatus();
		double resultAmountUsed = creditCard.getAmountUsed();
		
		
		//assert
		assertEquals(expectedType, resultType);
		assertEquals(expectedLimit, resultLimit);
		assertEquals(expectedPin, resultPin);
		assertEquals(expectedUser, resultUser);
		assertEquals(expectedStatus, resultStatus);
		assertEquals(expectedAmountUsed, resultAmountUsed);
    }
	
	@Test
	@DisplayName("Two objects instansiated with the same argument are equals to each other")
    public void testEqualsSameFields() {
		creditCard1 = new CreditCard("1234-5678-1234-5678", "123", 3000, "Ultimate Cashback Card", status, 0, user);
        assertTrue(creditCard.equals(creditCard1));
    }

    @Test
    @DisplayName("Two objects instansiated with different argument are different to each other")
    public void testEquals_DifferentFields() {
    	creditCard1 = new CreditCard("1235-5678-1234-5378", "125", 2000, "Ultimate Cashback Card", status, 0, user);
        assertFalse(creditCard.equals(creditCard1));
    } 
    
    @Test
    @DisplayName("addTransaction function increases amount used when trasaction is added")
    public void testAddTransaction() {
    	double expectedAmountUsed = 100;
    	creditCard.addTransaction(100);
    	double resultAmountUsed = creditCard.getAmountUsed();
    	
    	assertEquals(expectedAmountUsed, resultAmountUsed);
    }
}
