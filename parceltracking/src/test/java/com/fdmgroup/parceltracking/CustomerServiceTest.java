package com.fdmgroup.parceltracking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.parceltracking.model.Customer;
import com.fdmgroup.parceltracking.repository.CustomerRepository;
import com.fdmgroup.parceltracking.service.CustomerService;

@SpringBootTest
class CustomerServiceTest {

	private CustomerService customerService;

	@Mock
	private CustomerRepository mockRepo;

	@BeforeEach
	void setUp() {
		customerService = new CustomerService(mockRepo);
	}

	@Test
	@DisplayName("Test to check that persist method saves customer to database")
	void test1() {
		// Arrange
		Customer customer = new Customer("BLK 64 YUNG KUANG ROAD, #01-113", "john.doe", "johndoe123", "John", "Doe",
				null, null);
		// Act
		customerService.persist(customer);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByUsername("john.doe");
		order.verify(mockRepo).save(customer);
		order.verifyNoMoreInteractions();
	}

	@Test
	@DisplayName("Test to check if Customer is in Database")
	void test2() {
		// Arrange
		Customer customer = new Customer("BLK 64 YUNG KUANG ROAD, #01-113", "john.doe", "johndoe123", "John", "Doe",
				null, null);
		Optional<Customer> tempCustomer = Optional.ofNullable(customer);
		when(mockRepo.findByUsername("john.doe")).thenReturn(tempCustomer);
		// Act
		boolean check = customerService.usernameInDatabase("john.doe");
		// Assert
		assertEquals(true, check);
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByUsername("john.doe");
		order.verifyNoMoreInteractions();
	}

	@Test
	@DisplayName("Test for passwordchecker method with wrong password")
	void test3() {
		// Arrange
		Customer customer = new Customer("BLK 64 YUNG KUANG ROAD, #01-113", "john.doe", "johndoe123", "John", "Doe",
				null, null);
		String actualPassword = "";
		// Act
		boolean check = customerService.passwordChecker(customer.getUsername(), actualPassword);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByUsername("john.doe");
		order.verifyNoMoreInteractions();
		assertEquals(false, check);
	}

	@Test
	@DisplayName("Test for passwordchecker method with wrong username")
	void test4() {
		// Arrange
		Customer customer = new Customer("BLK 64 YUNG KUANG ROAD, #01-113", "john.doe", "johndoe123", "John", "Doe",
				null, null);
		String actualUsername = "";
		// Act
		boolean check = customerService.passwordChecker(actualUsername, customer.getPassword());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByUsername("");
		order.verifyNoMoreInteractions();
		assertEquals(false, check);
	}

	@Test
	@DisplayName("Test for passwordchecker method with correct password and username")
	void test5() {
		// Arrange
		Customer customer = new Customer("BLK 64 YUNG KUANG ROAD, #01-113", "john.doe", "johndoe123", "John", "Doe",
				null, null);
		Optional<Customer> tempCustomer = Optional.ofNullable(customer);
		when(mockRepo.findByUsername(customer.getUsername())).thenReturn(tempCustomer);
		// Act
		boolean check = customerService.passwordChecker(customer.getUsername(), customer.getPassword());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByUsername("john.doe");
		order.verifyNoMoreInteractions();
		assertEquals(true, check);
	}

	@Test
	@DisplayName("Test for findParcelsByUsername method retrieves parcel details from database")
	void test6() {

		// Arrange
		Customer customer = new Customer("BLK 64 YUNG KUANG ROAD, #01-113", "john.doe", "johndoe123", "John", "Doe",
				null, null);
		Optional<Customer> tempCustomer = Optional.ofNullable(customer);
		when(mockRepo.findByUsername(customer.getUsername())).thenReturn(tempCustomer);
		// Act
		customerService.findParcelsByUsername(customer.getUsername());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByUsername(customer.getUsername());
		order.verifyNoMoreInteractions();
	}

	@Test
	@DisplayName("Test for findCustomerById method when customer exists in database")
	void test7() {
		// Arrange
		long id = 4L;
		Customer expectedCustomer = new Customer("BLK 64 YUNG KUANG ROAD, #01-113", "john.doe", "johndoe123", "John",
				"Doe", null, null);
		expectedCustomer.setId(id);
		Optional<Customer> tempCustomer = Optional.ofNullable(expectedCustomer);
		when(mockRepo.findById(id)).thenReturn(tempCustomer);

		// Act
		Customer actualCustomer = customerService.findCustomerById(id);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(id);
		order.verifyNoMoreInteractions();
		assertEquals(expectedCustomer, actualCustomer);
	}

	@Test
	@DisplayName("Test for findCustomerById method when customer doesn't exist in database")
	void test8() {
		// Arrange
		long id = 10L;
		Customer expectedCustomer = null;
		// Act
		Customer actualCustomer = customerService.findCustomerById(id);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(id);
		order.verifyNoMoreInteractions();
		assertEquals(expectedCustomer, actualCustomer);
	}

	@Test
	@DisplayName("Test for findCustomerByUsername method when customer exists in database")
	void test9() {
		// Arrange
		Customer expectedCustomer = new Customer("BLK 64 YUNG KUANG ROAD, #01-113", "john.doe", "johndoe123", "John",
				"Doe", null, null);
		Optional<Customer> tempCustomer = Optional.ofNullable(expectedCustomer);
		when(mockRepo.findByUsername(expectedCustomer.getUsername())).thenReturn(tempCustomer);
		// Act
		Customer actualCustomer = customerService.findCustomerByUsername(expectedCustomer.getUsername());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByUsername(expectedCustomer.getUsername());
		order.verifyNoMoreInteractions();
		assertEquals(expectedCustomer, actualCustomer);
	}

	@Test
	@DisplayName("Test for findCustomerByUsername method when customer doesn't exist in database")
	void test10() {
		// Arrange
		String username = "test";
		Customer expectedCustomer = null;
		// Act
		Customer actualCustomer = customerService.findCustomerByUsername(username);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByUsername(username);
		order.verifyNoMoreInteractions();
		assertEquals(expectedCustomer, actualCustomer);
	}

	@Test
	@DisplayName("Test for update method when customer exists in database")
	void test11() {
		// Arrange
		Customer expectedCustomer = new Customer("BLK 64 YUNG KUANG ROAD, #01-113", "john.doe", "johndoe123", "John",
				"Doe", null, null);
		Optional<Customer> tempCustomer = Optional.ofNullable(expectedCustomer);
		when(mockRepo.findByUsername(expectedCustomer.getUsername())).thenReturn(tempCustomer);
		// Act
		customerService.update(expectedCustomer);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByUsername(expectedCustomer.getUsername());
		order.verify(mockRepo).save(expectedCustomer);
		order.verifyNoMoreInteractions();
	}

	@Test
	@DisplayName("Test for update method when customer does not exist in database")
	void test12() {
		// Arrange
		String username = "test";
		Customer expectedCustomer = new Customer();
		expectedCustomer.setUsername(username);
		// Act
		customerService.update(expectedCustomer);
		// Arrange
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByUsername(username);
		order.verifyNoMoreInteractions();
		verify(mockRepo, never()).save(expectedCustomer);
	}

	@Test
	@DisplayName("Test for deleteById method when customer exists in database")
	void test13() {
		// Arrange
		Customer expectedCustomer = new Customer("BLK 64 YUNG KUANG ROAD, #01-113", "john.doe", "johndoe123", "John",
				"Doe", null, null);
		expectedCustomer.setId(10L);
		Optional<Customer> tempCustomer = Optional.ofNullable(expectedCustomer);
		when(mockRepo.findById(10L)).thenReturn(tempCustomer);
		// Act
		customerService.deleteById(10L);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(10L);
		order.verify(mockRepo).deleteById(10L);
		order.verifyNoMoreInteractions();
	}

	@Test
	@DisplayName("Test for deleteById method when customer does not exist in database")
	void test14() {
		// Arrange
		Customer expectedCustomer = new Customer("BLK 64 YUNG KUANG ROAD, #01-113", "john.doe", "johndoe123", "John",
				"Doe", null, null);
		expectedCustomer.setId(10L);
		// Act
		customerService.deleteById(10L);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(10L);
		order.verifyNoMoreInteractions();
		verify(mockRepo, never()).deleteById(10L);
	}
}
