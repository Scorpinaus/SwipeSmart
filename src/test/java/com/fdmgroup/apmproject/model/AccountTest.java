package com.fdmgroup.apmproject.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
	
	@Test
	@DisplayName("The intansiation of account class is of the same instance")
    public void testEqualsSameInstance() {
        assertTrue(account.equals(account));
    }
	
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
	
	@Test
	@DisplayName("Two objects instansiated with the same argument are equals to each other")
    public void testEqualsSameFields() {
		account1 = new Account("Savings", 1000, "123-123-123", user, status);
        assertTrue(account.equals(account1));
    }

    @Test
    @DisplayName("Two objects instansiated with different argument are different to each other")
    public void testEquals_DifferentFields() {
    	account1 = new Account("Savings2", 2000, "124-123-123", user, status);
        assertFalse(account.equals(account1));
    } 
}
