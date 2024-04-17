package com.fdmgroup.apmproject.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class MechantCateogoryCodeTest {
	
	
	//Global variable 
	MerchantCategoryCode mcc;
	MerchantCategoryCode mcc1;
	
	@BeforeEach
	public void setUp() {
        mcc = new MerchantCategoryCode(1, "Grocery");
        mcc1 = new MerchantCategoryCode(2, "Electronics");
    }
	
	@Test
	@DisplayName("The intansiation of MerchantCategoryCode class is of the same instance")
    public void testEqualsSameInstance() {
        assertTrue(mcc.equals(mcc));
    }
	
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
	
	@Test
	@DisplayName("Two objects instansiated with the same argument are equals to each other")
    public void testEqualsSameFields() {
		MerchantCategoryCode mcc2 = new MerchantCategoryCode(1, "Grocery");
        assertTrue(mcc.equals(mcc2));
    }
	
	@Test
    @DisplayName("Two objects instansiated with different argument are different to each other")
    public void testEquals_DifferentFields() {
        assertFalse(mcc.equals(mcc1));
    }
}
