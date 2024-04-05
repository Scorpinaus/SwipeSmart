package com.fdmgroup.parceltracking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fdmgroup.parceltracking.model.Customer;
import com.fdmgroup.parceltracking.model.Parcel;

class CustomerTest {

	private Customer customer;

	@BeforeEach
	void setUp() {
		customer = new Customer("BLK 64 YUNG KUANG ROAD, #01-113", "john.doe", "johndoe123", "John", "Doe", null, null);
	}

	@Test
	@DisplayName("Test if setParcels method adds parcels to customer's list of parcels")
	void test() {
		// Arrange
		Parcel expectedParcel = null;

		// Act
		customer.setParcels(expectedParcel);

		// Assert
		for (Parcel p : customer.getParcels()) {
			assertEquals(p, expectedParcel);
		}
	}
}
