package com.fdmgroup.apmproject.service;

import static org.junit.jupiter.api.Assertions.*;
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

import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.repository.UserRepository;
import com.fdmgroup.apmproject.service.UserService;

/**
 * Conducts unit tests for the UserService class using a mocked UserRepository.
 * <p>
 * This test class is designed to perform various tests on the UserService
 * methods to ensure they function correctly with given user data. It includes
 * setup before each test where a new instance of UserService is initialized
 * with a mocked UserRepository, allowing for controlled testing of service
 * method interactions with the database.
 *
 * @see UserService For the service class being tested which handles business
 *      logic associated with user operations.
 * @see UserRepository For the repository interface used to access and
 *      manipulate user data.
 */

@SpringBootTest
class UserServiceTest {

	private UserService userService;

	@Mock
	private UserRepository mockRepo;

	@BeforeEach
	void setUp() {
		userService = new UserService(mockRepo);
	}

	/**
	 * Tests the persist method of UserService for user creation and data
	 * persistence.
	 * <p>
	 * This test method ensures that the UserService persist method correctly
	 * interacts with the UserRepository for storing user data. It begins by setting
	 * up a User object with specified username and name fields, then it calls the
	 * persist method on the UserService. The method verifies the order of
	 * interactions with the repository: first checking if the username exists, then
	 * saving the new user, and finally confirming no further interactions occur.
	 * This test ensures the persist method's compliance with expected database
	 * interaction protocols.
	 *
	 * @param None There are no parameters for this test method.
	 * @return None This method does not return a value as it is a test method.
	 * @throws None This method does not throw any exceptions.
	 * @see UserService#persist(User) To understand the method being tested.
	 * @see UserRepository#findByUsername(String), UserRepository#save(User) To
	 *      understand the repository methods being interacted with during the test.
	 */
	@Test
	@DisplayName("Test for persist method")
	void test1() {
		// Arrange
		User user = new User("andrew.tan", null, null, "Andrew", "Tan");
		// Act
		userService.persist(user);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByUsername("andrew.tan");
		order.verify(mockRepo).save(user);
		order.verifyNoMoreInteractions();
	}

	/**
	 * Tests the update method of UserService to confirm user updates when the user
	 * already exists in the database.
	 * <p>
	 * This method arranges a predefined User object and mocks the retrieval of this
	 * User from the UserRepository to simulate the user existing in the database.
	 * It then tests the update functionality by invoking the update method on the
	 * UserService. The test verifies the sequence of repository interactions: it
	 * first confirms the user's existence by username, then it saves the updated
	 * user details, and concludes by ensuring no further repository interactions
	 * occur. This method checks the proper function of update operations under
	 * expected use conditions.
	 *
	 * @param None There are no parameters for this test method.
	 * @return None This method does not return a value as it is a test method.
	 * @throws None This method does not throw any exceptions.
	 * @see UserService#update(User) To understand the method being tested.
	 * @see UserRepository#findByUsername(String), UserRepository#save(User) To
	 *      understand the repository methods being interacted with during the test.
	 */
	@Test
	@DisplayName("Test for update method when User exists in database")
	void test2() {
		// Arrange
		User expectedUser = new User("andrew.tan", null, null, "Andrew", "Tan");
		Optional<User> tempUser = Optional.ofNullable(expectedUser);
		when(mockRepo.findByUsername(expectedUser.getUsername())).thenReturn(tempUser);
		// Act
		userService.update(expectedUser);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByUsername(expectedUser.getUsername());
		order.verify(mockRepo).save(expectedUser);
		order.verifyNoMoreInteractions();
	}

	/**
	 * Tests the update method of UserService to verify behavior when the user does
	 * not exist in the database.
	 * <p>
	 * This test method simulates an attempt to update a User that is not present in
	 * the database. It verifies that the UserService correctly checks for the
	 * user's existence via the repository and does not proceed to save any updates
	 * due to the user's absence. The method checks for the correct sequence of
	 * method calls: a username lookup and the absence of a save operation. This
	 * ensures that the update method behaves as expected when the target user
	 * cannot be found in the database.
	 *
	 * @param None There are no parameters for this test method.
	 * @return None This method does not return a value as it is a test method.
	 * @throws None This method does not throw any exceptions.
	 * @see UserService#update(User) To understand the method being tested.
	 * @see UserRepository#findByUsername(String) To understand the repository
	 *      method used to check user existence.
	 */
	@Test
	@DisplayName("Test for update method when User does not exist in database")
	void test3() {
		// Arrange
		User expectedUser = new User("andrew.tan", null, null, "Andrew", "Tan");
		// Act
		userService.update(expectedUser);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByUsername(expectedUser.getUsername());
		order.verifyNoMoreInteractions();
		verify(mockRepo, never()).save(expectedUser);
	}

	/**
	 * Tests the findUserById method of UserService to ensure it correctly retrieves
	 * a user when the user exists in the database.
	 * <p>
	 * This method sets up a User object with a specific ID and mocks the retrieval
	 * process using the UserRepository to simulate finding a user by ID in the
	 * database. It asserts that the UserService returns the correct user object by
	 * comparing the expected and actual user details. The test verifies the
	 * sequence of method calls to the repository: a single ID lookup followed by
	 * confirming no further interactions, ensuring that findUserById is functioning
	 * correctly in the presence of the user.
	 *
	 * @param None There are no parameters for this test method.
	 * @return None This method does not return a value as it is a test method.
	 * @throws None This method does not throw any exceptions.
	 * @see UserService#findUserById(Long) To understand the method being tested.
	 * @see UserRepository#findById(Long) To understand the repository method used
	 *      to fetch user data.
	 */
	@Test
	@DisplayName("Test for findUserById method when User exists in database")
	void test4() {
		// Arrange
		User expectedUser = new User("andrew.tan", null, null, "Andrew", "Tan");
		expectedUser.setUserId(14L);
		Optional<User> tempUser = Optional.ofNullable(expectedUser);
		when(mockRepo.findById(14L)).thenReturn(tempUser);
		// Act
		User actualUser = userService.findUserById(14L);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(14L);
		order.verifyNoMoreInteractions();
		assertEquals(expectedUser, actualUser);
	}

	/**
	 * Tests the findUserById method of UserService to verify it returns null when
	 * the user does not exist in the database.
	 * <p>
	 * This method assesses the behavior of UserService's findUserById when
	 * attempting to retrieve a user by an ID that does not correspond to any
	 * existing user in the database. It ensures that the method properly returns
	 * null, indicating no user found, and verifies that the only interaction with
	 * the UserRepository is a single ID check. This test validates that the method
	 * handles non-existent users correctly by not performing unnecessary actions or
	 * interactions beyond the ID query.
	 *
	 * @param None There are no parameters for this test method.
	 * @return None This method does not return a value as it is a test method.
	 * @throws None This method does not throw any exceptions.
	 * @see UserService#findUserById(Long) To understand the method being tested.
	 * @see UserRepository#findById(Long) To understand the repository method used
	 *      to verify user existence.
	 */
	@Test
	@DisplayName("Test for findUserById method when User does not exist in database")
	void test5() {
		// Arrange
		User expectedUser = null;
		// Act
		User actualUser = userService.findUserById(2L);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(2L);
		order.verifyNoMoreInteractions();
		assertEquals(expectedUser, actualUser);
	}

	/**
	 * Tests the findUserByUsername method of UserService to ensure it correctly
	 * retrieves a user when the username exists in the database.
	 * <p>
	 * This method sets up a User object with a specific username and mocks the
	 * retrieval process using the UserRepository to simulate finding a user by
	 * username in the database. It asserts that the UserService returns the correct
	 * user object by comparing the expected and actual user details. The test
	 * verifies the sequence of method calls to the repository: a single username
	 * lookup followed by confirming no further interactions, ensuring that
	 * findUserByUsername is functioning correctly in the presence of the user.
	 *
	 * @param None There are no parameters for this test method.
	 * @return None This method does not return a value as it is a test method.
	 * @throws None This method does not throw any exceptions.
	 * @see UserService#findUserByUsername(String)
	 */
	@Test
	@DisplayName("Test for findUserByUsername method when User exists in database")
	void test6() {
		// Arrange
		User expectedUser = new User("andrew.tan", null, null, "Andrew", "Tan");
		Optional<User> tempUser = Optional.ofNullable(expectedUser);
		when(mockRepo.findByUsername(expectedUser.getUsername())).thenReturn(tempUser);
		// Act
		User actualUser = userService.findUserByUsername(expectedUser.getUsername());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByUsername(expectedUser.getUsername());
		order.verifyNoMoreInteractions();
		assertEquals(expectedUser, actualUser);
	}

	/**
	 * Tests the findUserByUsername method of UserService to verify it returns null
	 * when the user does not exist in the database.
	 * <p>
	 * This test method evaluates the behavior of UserService's findUserByUsername
	 * when searching for a username that does not correspond to any existing user
	 * in the database. It ensures that the method correctly returns null, signaling
	 * that no user is found, and verifies that the only interaction with the
	 * UserRepository is a single username check. This test confirms that the method
	 * handles the scenario of a non-existent user appropriately, avoiding
	 * unnecessary actions or interactions beyond the initial search.
	 *
	 * @param None There are no parameters for this test method.
	 * @return None This method does not return a value as it is a test method.
	 * @throws None This method does not throw any exceptions.
	 * @see UserService#findUserByUsername(String) To understand the method being
	 *      tested.
	 * @see UserRepository#findByUsername(String) To understand the repository
	 *      method used to verify user existence.
	 */
	@Test
	@DisplayName("Test for findUserByUsername method when User does not exist in database")
	void test7() {
		// Arrange
		User expectedUser = null;
		// Act
		User actualUser = userService.findUserByUsername("test");
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByUsername("test");
		order.verifyNoMoreInteractions();
		assertEquals(expectedUser, actualUser);
	}

	/**
	 * Tests the deleteById method of UserService to ensure it correctly deletes a
	 * user when the user exists in the database.
	 * <p>
	 * This method arranges a User object with a specified ID, mocks the retrieval
	 * process using the UserRepository to simulate the existence of the user in the
	 * database, and tests the delete functionality. It verifies that the
	 * UserService properly executes the delete operation by checking the sequence
	 * of repository interactions: first confirming the user's existence by ID, then
	 * proceeding with the deletion, and concluding with no further interactions.
	 * This test validates the deleteById method's functionality under expected
	 * conditions.
	 *
	 * @param None There are no parameters for this test method.
	 * @return None This method does not return a value as it is a test method.
	 * @throws None This method does not throw any exceptions.
	 * @see UserService#deleteById(Long) To understand the method being tested.
	 * @see UserRepository#findById(Long), UserRepository#deleteById(Long) To
	 *      understand the repository methods involved in fetching and deleting user
	 *      data.
	 */
	@Test
	@DisplayName("Test for deleteById method when User exists in database")
	void test8() {
		// Arrange
		User expectedUser = new User("andrew.tan", null, null, "Andrew", "Tan");
		expectedUser.setUserId(12L);
		Optional<User> tempUser = Optional.ofNullable(expectedUser);
		when(mockRepo.findById(12L)).thenReturn(tempUser);
		// Act
		userService.deleteById(12L);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(12L);
		order.verify(mockRepo).deleteById(12L);
		order.verifyNoMoreInteractions();

	}

	/**
	 * Tests the deleteById method of UserService to ensure it does not attempt
	 * deletion when the user does not exist in the database.
	 * <p>
	 * This method simulates the deletion attempt of a non-existing user by
	 * providing a user ID that is not found in the database. It checks that
	 * UserService does not perform a delete operation by verifying the sequence of
	 * repository interactions: it confirms the absence of the user by querying the
	 * ID and ensures no deletion is attempted. This test validates the method's
	 * behavior in handling cases where the user ID does not correspond to any
	 * database record, thus preventing incorrect data manipulation.
	 *
	 * @param None There are no parameters for this test method.
	 * @return None This method does not return a value as it is a test method.
	 * @throws None This method does not throw any exceptions.
	 * @see UserService#deleteById(Long) To understand the method being tested.
	 * @see UserRepository#findById(Long) To understand the repository method used
	 *      to check user existence before attempting deletion.
	 */
	@Test
	@DisplayName("Test for deleteById method when User does not exist in database")
	void test9() {
		// Arrange
		User expectedUser = new User();
		expectedUser.setUserId(9L);
		// Act
		userService.deleteById(expectedUser.getUserId());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(9L);
		order.verifyNoMoreInteractions();
		verify(mockRepo, never()).deleteById(9L);
	}
}
