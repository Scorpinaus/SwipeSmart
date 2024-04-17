package com.fdmgroup.apmproject.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StatusTest {
	
	//Global Variables 
	Status status;
	Status status2;
	
	@BeforeEach
	public void setUp() {
		status = new Status("Pending");
		status2 = new Status("Approved");
	}
	
	@Test
	@DisplayName("The intansiation of Status class is of the same instance")
    public void testEqualsSameInstance() {
        assertTrue(status.equals(status));
    }
	
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
	
	@Test
	@DisplayName("Two objects instansiated with the same argument are equals to each other")
    public void testEqualsSameFields() {
		Status status3 = new Status("Pending");
        assertTrue(status.equals(status3));
    }
	
 	@Test
    @DisplayName("Two objects instansiated with different argument are different to each other")
    public void testEquals_DifferentFields() {
        assertFalse(status.equals(status2));
    }
}
