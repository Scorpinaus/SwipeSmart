package com.fdmgroup.apmproject.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fdmgroup.apmproject.controller.AccountController;

/**
 * Test suite for {@link Account} for unit testing of account entities.
 *
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */

public class AccountTest {
	
	//Global Variable 
	User user;
	Account account;
	Status status;
	Account account1;
	
	
	@BeforeEach
	public void setUp() {
		user = new User("jackytan", "Qwerty1", "Sentosa", "Jacky", "Tan");
		status = new Status("Approved");
		account = new Account("Savings", 1000, "123-123-123", user, status);
		
	}
	
	/**
	 * Tests whether the instantiation of the Account class is the same instance.
	 * Verifies that an account object is equal to itself.
	 *
	 * @return True if the account object is equal to itself, otherwise false.
	 * @see Account#equals(Object)
	 */
	@Test
	@DisplayName("The intansiation of account class is of the same instance")
    public void testEqualsSameInstance() {
        assertTrue(account.equals(account));
    }
	
	/**
	 * Tests whether the instantiation of the Account class gives the expected attribute values.
	 * Compares the attributes of the account instance with the expected values.
	 *
	 * @return True if all attributes match the expected values, otherwise false.
	 * @see Account#getAccountName()
	 * @see Account#getBalance()
	 * @see Account#getAccountNumber()
	 * @see Account#getAccountUser()
	 * @see Account#getAccountStatus()
	 */
	@Test
	@DisplayName("The intansiation of account class gives the same attribute values")
    public void testUserValue() {
		//arrange
		String expectedName = "Savings";
		double expectedBalance = 1000;
		String expectedNumber = "123-123-123";
		User expectedUser = user;
		Status expectedStatus = status;
		
		//act
		String resultName = account.getAccountName();
		double resultBalance = account.getBalance();
		String resultNumber = account.getAccountNumber();
		User resultUser = account.getAccountUser();
		Status resultStatus = account.getAccountStatus();
		
		
		//assert
		assertEquals(expectedName, resultName);
		assertEquals(expectedBalance, resultBalance);
		assertEquals(expectedNumber, resultNumber);
		assertEquals(expectedUser, resultUser);
		assertEquals(expectedStatus, resultStatus);
    }
	
	/**
	 * Tests whether two objects instantiated with the same arguments are equal to each other.
	 * Compares two account instances instantiated with the same fields for equality.
	 *
	 * @return True if the two objects are equal, otherwise false.
	 * @see Account#equals(Object)
	 */
	@Test
	@DisplayName("Two objects instansiated with the same argument are equals to each other")
    public void testEqualsSameFields() {
		account1 = new Account("Savings", 1000, "123-123-123", user, status);
        assertTrue(account.equals(account1));
    }

	/**
	 * Tests whether two objects instantiated with different arguments are different from each other.
	 * Compares two account instances instantiated with different fields for inequality.
	 *
	 * @return True if the two objects are not equal, otherwise false.
	 * @see Account#equals(Object)
	 */
    @Test
    @DisplayName("Two objects instansiated with different argument are different to each other")
    public void testEquals_DifferentFields() {
    	account1 = new Account("Savings2", 2000, "124-123-123", user, status);
        assertFalse(account.equals(account1));
    } 
}
