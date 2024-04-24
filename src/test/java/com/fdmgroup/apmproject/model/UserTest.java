package com.fdmgroup.apmproject.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {
	
	//Global Variable 
	User user;
	User user1;
	
	@BeforeEach
	public void setUp() {
		user = new User("jackytan", "Qwerty1", "Sentosa", "Jacky", "Tan");
	}
	
	/**
	 * Tests if the instantiation of the user class results in the same instance.
	 *
	 * @return True if the instantiation of the user class results in the same instance, otherwise false.
	 * @throws ExceptionType If the method encounters an unexpected condition.
	 * @see User
	 */
	@Test
	@DisplayName("The intansiation of user class is of the same instance")
    public void testEqualsSameInstance() {
        assertTrue(user.equals(user));
    }
	
	/**
	 * Tests if the instantiation of the user class provides the same attribute values.
	 *
	 * @return True if the instantiation of the user class provides the same attribute values, otherwise false.
	 * @throws ExceptionType If the method encounters an unexpected condition.
	 * @see User
	 */
	@Test
	@DisplayName("The intansiation of user class gives the same attribute values")
    public void testUserValue() {
		//arrange
		String expectedUsername = "jackytan";
		String expectedPassword = "Qwerty1";
		String expectedAddress = "Sentosa";
		String expectedFName = "Jacky";
		String expectedLName = "Tan";
		
		//act
		String resultUsername = user.getUsername();
		String resultPassword = user.getPassword();
		String resultAddress = user.getAddress();
		String resultFName = user.getFirstName();
		String resultLName = user.getLastName();
		
		
		//assert
		assertEquals(expectedUsername, resultUsername);
		assertEquals(expectedPassword, resultPassword);
		assertEquals(expectedAddress, resultAddress);
		assertEquals(expectedFName, resultFName);
		assertEquals(expectedLName, resultLName);
    }
	
	/**
	 * Tests if two objects instantiated with the same arguments are equal to each other.
	 *
	 * @return True if two objects instantiated with the same arguments are equal to each other, otherwise false.
	 * @throws ExceptionType If the method encounters an unexpected condition.
	 * @see User
	 */
	@Test
	@DisplayName("Two objects instansiated with the same argument are equals to each other")
    public void testEqualsSameFields() {
		user1 = new User("jackytan", "Qwerty1", "Sentosa", "Jacky", "Tan");
        assertTrue(user.equals(user1));
    }

	/**
	 * Tests if two objects instantiated with different arguments are different from each other.
	 *
	 * @return True if two objects instantiated with different arguments are different from each other, otherwise false.
	 * @throws ExceptionType If the method encounters an unexpected condition.
	 * @see User
	 */
    @Test
    @DisplayName("Two objects instansiated with different argument are different to each other")
    public void testEquals_DifferentFields() {
    	user1 = new User("jane", "Qwerety1", "Sentosa", "Jan", "Tan");
        assertFalse(user.equals(user1));
    } 
}
