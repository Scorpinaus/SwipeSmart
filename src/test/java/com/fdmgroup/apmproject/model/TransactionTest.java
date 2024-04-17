package com.fdmgroup.apmproject.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class TransactionTest {

	// Global Variable
	User user;
	CreditCard creditCard;
	Status status;
	Account account;
	Transaction transaction;
	Transaction transaction1;
	MerchantCategoryCode mcc;

	@BeforeEach
	public void setUp() {
		user = new User("jackytan", "Qwerty1", "Sentosa", "Jacky", "Tan");
		status = new Status("Approved");
		String creditCardNumber = "1234-5678-1234-5678";
		String pin = "123";
		creditCard = new CreditCard(creditCardNumber, pin, 3000, "Ultimate Cashback Card", status, 0, user);
		account = new Account("Savings", 1000, "123-123-123", user, status);
		mcc = new MerchantCategoryCode(1, "Grocery");
		transaction = new Transaction("Withdraw", 10, null, 0, null, account, null, null);
		transaction1 = new Transaction("Withdraw", 10, null, 1, creditCard, null, mcc, null);
	}
	
	@Test
	@DisplayName("The intansiation of transaction class is of the same instance for both account and credit card transaction")
    public void testEqualsSameInstance() {
        assertTrue(transaction.equals(transaction));
        assertTrue(transaction1.equals(transaction1));
    }
	
	@Test
	@DisplayName("The intansiation of trasaction class gives the same attribute values")
    public void testTransactionValue() {
		//arrange
		CreditCard expectedCreditCard = creditCard;
		MerchantCategoryCode expectedMCC = mcc;
		double expectedAmount = 10;
		double expectedCashback = 1;
		String transactionType = "Withdraw";
		
		
		//act
		CreditCard resultCreditCard = transaction1.getTransactionCreditCard();
		Charity resultCharity = donation1.getCharity();
		BigDecimal resultAmount = donation1.getAmount();
		
		
		//assert
		assertEquals(expectedUser, resultUser);
		assertEquals(expectedCharity, resultCharity);
		assertEquals(expectedAmount, resultAmount);
    }
	
	@Test
	@DisplayName("Same user can make multiple donations")
    public void testMultipleDonation() {
		//arrange
		User expectedUser = user1;
		
		//act
		User resultUser1 = donation1.getUser();
		User resultUser2 = donation2.getUser();
		
		//assert
		assertEquals(expectedUser, resultUser1);
		assertEquals(expectedUser, resultUser2);
	}
	
	@Test
	@DisplayName("Two objects instansiated with the same argument are equals to each other")
    public void testEqualsSameFields() {
        Donation donation3 = new Donation(user1,mockCharity1);
		assertTrue(donation1.equals(donation3));
    }

    @Test
    @DisplayName("Two objects instansiated with different argument are different to each other")
    public void testEquals_DifferentFields() {
        assertFalse(donation1.equals(donation2));
    }
}
