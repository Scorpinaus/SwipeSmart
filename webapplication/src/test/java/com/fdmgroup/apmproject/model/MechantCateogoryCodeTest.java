package com.fdmgroup.apmproject.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link MerchantCategoryCode} for unit testing of merchant category code entities.
 *
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */

public class MechantCateogoryCodeTest {
	
	
	//Global variable 
	MerchantCategoryCode mcc;
	MerchantCategoryCode mcc1;
	
	@BeforeEach
	public void setUp() {
        mcc = new MerchantCategoryCode(1, "Grocery");
        mcc1 = new MerchantCategoryCode(2, "Electronics");
    }
	
	/**
	 * Tests if the instantiation of the MerchantCategoryCode class results in the same instance.
	 *
	 * @return True if the instantiation results in the same instance, otherwise false.
	 * @throws ExceptionType If the method encounters an unexpected condition.
	 * @see MerchantCategoryCode
	 */
	@Test
	@DisplayName("The intansiation of MerchantCategoryCode class is of the same instance")
    public void testEqualsSameInstance() {
        assertTrue(mcc.equals(mcc));
    }
	
	/**
	 * Tests if the instantiation of the MerchantCategoryCode class results in the same attribute values.
	 *
	 * @return True if the instantiation results in the same attribute values, otherwise false.
	 * @throws ExceptionType If the method encounters an unexpected condition.
	 * @see MerchantCategoryCode
	 */
	@Test
	@DisplayName("The intansiation of MerchantCategoryCode class gives the same attribute values")
    public void testMccValue() {
		//arrange
		String expectedCategory = "Grocery";
		int expectedNumber = 1;

		//act
		String resultCategory = mcc.getMerchantCategory();
		int resultNumber = mcc.getMerchantCategoryCodeNumber();
		
		
		//assert
		assertEquals(expectedCategory, resultCategory);
		assertEquals(expectedNumber, resultNumber);
    }
	
	/**
	 * Tests if two objects instantiated with the same argument are equal to each other.
	 *
	 * @return True if the two objects instantiated with the same argument are equal, otherwise false.
	 * @throws ExceptionType If the method encounters an unexpected condition.
	 * @see MerchantCategoryCode
	 */
	@Test
	@DisplayName("Two objects instansiated with the same argument are equals to each other")
    public void testEqualsSameFields() {
		MerchantCategoryCode mcc2 = new MerchantCategoryCode(1, "Grocery");
        assertTrue(mcc.equals(mcc2));
    }
	
	/**
	 * Tests if two objects instantiated with different arguments are different from each other.
	 *
	 * @return True if the two objects instantiated with different arguments are different, otherwise false.
	 * @throws ExceptionType If the method encounters an unexpected condition.
	 * @see MerchantCategoryCode
	 */
	@Test
    @DisplayName("Two objects instansiated with different argument are different to each other")
    public void testEquals_DifferentFields() {
        assertFalse(mcc.equals(mcc1));
    }
}
