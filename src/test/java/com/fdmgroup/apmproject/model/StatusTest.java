package com.fdmgroup.apmproject.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link Status} for unit testing of status entities.
 *
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */

public class StatusTest {
	
	//Global Variables 
	Status status;
	Status status2;
	
	@BeforeEach
	public void setUp() {
		status = new Status("Pending");
		status2 = new Status("Approved");
	}
	
	/**
	 * Tests if the instantiation of the Status class results in the same instance.
	 *
	 * @return True if the instantiation of the Status class results in the same instance, otherwise false.
	 * @throws ExceptionType If the method encounters an unexpected condition.
	 * @see Status
	 */
	@Test
	@DisplayName("The intansiation of Status class is of the same instance")
    public void testEqualsSameInstance() {
        assertTrue(status.equals(status));
    }
	
	/**
	 * Tests if the instantiation of the Status class results in the expected attribute values.
	 *
	 * @return True if the instantiation of the Status class results in the expected attribute values, otherwise false.
	 * @throws ExceptionType If the method encounters an unexpected condition.
	 * @see Status
	 */
	@Test
	@DisplayName("The intansiation of Status class gives the same attribute values")
    public void testStatusValue() {
		//arrange
		String expectedName = "Pending";

		//act
		String resultName = status.getStatusName();		
		
		//assert
		assertEquals(expectedName, resultName);
    }
	
	/**
	 * Tests if two objects instantiated with the same argument are equal to each other.
	 *
	 * @return True if two objects instantiated with the same argument are equal to each other, otherwise false.
	 * @throws ExceptionType If the method encounters an unexpected condition.
	 * @see Status
	 */
	@Test
	@DisplayName("Two objects instansiated with the same argument are equals to each other")
    public void testEqualsSameFields() {
		Status status3 = new Status("Pending");
        assertTrue(status.equals(status3));
    }
	
	/**
	 * Tests if two objects instantiated with different arguments are different from each other.
	 *
	 * @return True if two objects instantiated with different arguments are different from each other, otherwise false.
	 * @throws ExceptionType If the method encounters an unexpected condition.
	 * @see Status
	 */
 	@Test
    @DisplayName("Two objects instansiated with different argument are different to each other")
    public void testEquals_DifferentFields() {
        assertFalse(status.equals(status2));
    }
}
