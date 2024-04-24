package com.fdmgroup.apmproject.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link Transaction} for unit testing of transaction entities.
 *
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */

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
	
	/**
	 * Tests if the instantiation of a transaction class is of the same instance for both account and credit card transactions.
	 *
	 * @return True if the instantiation of the transaction class is of the same instance for both account and credit card transactions, otherwise false.
	 * @throws ExceptionType If the method encounters an unexpected condition.
	 * @see Transaction
	 */
	@Test
	@DisplayName("The intansiation of transaction class is of the same instance for both account and credit card transaction")
    public void testEqualsSameInstance() {
        assertTrue(transaction.equals(transaction));
        assertTrue(transaction1.equals(transaction1));
    }
	
	/**
	 * Tests if the instantiation of a transaction class gives the same attribute values.
	 *
	 * @return True if the instantiation of the transaction class gives the same attribute values, otherwise false.
	 * @throws ExceptionType If the method encounters an unexpected condition.
	 * @see Transaction
	 */
	@Test
	@DisplayName("The intansiation of trasaction class gives the same attribute values")
    public void testTransactionValue() {
		//arrange
		CreditCard expectedCreditCard = creditCard;
		MerchantCategoryCode expectedMCC = mcc;
		double expectedAmount = 10;
		double expectedCashback = 1;
		String expectedtransactionType = "Withdraw";
		
		
		//act
		CreditCard resultCreditCard = transaction1.getTransactionCreditCard();
		MerchantCategoryCode resultMCC = transaction1.getTransactionMerchantCategoryCode();
		double resultAmount = transaction1.getTransactionAmount();
		double resultCashback = transaction1.getCashback();
		String resultTransactionType = transaction1.getTransactionType();
		
		
		//assert
		assertEquals(expectedCreditCard, resultCreditCard);
		assertEquals(expectedMCC, resultMCC);
		assertEquals(expectedAmount, resultAmount);
		assertEquals(expectedCashback, resultCashback);
		assertEquals(expectedtransactionType, resultTransactionType);
    }
	
	/**
	 * Tests if the transaction shows the correct credit card or amount.
	 *
	 * @return True if the transaction shows the correct credit card or amount, otherwise false.
	 * @throws ExceptionType If the method encounters an unexpected condition.
	 * @see Transaction
	 */
	@Test
	@DisplayName("The transaction shows the correct credit card or amount")
    public void testTransaction() {
		//arrange
		CreditCard expectedCreditCard = creditCard;
		Account expectedAccount = account;
		
		//act
		CreditCard resultCreditCard = transaction1.getTransactionCreditCard();
		Account resultAccount = transaction.getTransactionAccount();
		
		//assert
		assertEquals(expectedCreditCard, resultCreditCard);
		assertEquals(expectedAccount, resultAccount);
	}
	
	/**
	 * Tests if two transactions instantiated with different arguments are different from each other.
	 *
	 * @return True if two transactions instantiated with different arguments are different from each other, otherwise false.
	 * @throws ExceptionType If the method encounters an unexpected condition.
	 * @see Transaction
	 */
    @Test
    @DisplayName("Two objects instansiated with different argument are different to each other")
    public void testEquals_DifferentFields() {
        assertFalse(transaction.equals(transaction1));
    }
}
