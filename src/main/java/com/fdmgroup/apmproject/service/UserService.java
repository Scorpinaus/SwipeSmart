package com.fdmgroup.apmproject.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.repository.UserRepository;

/**
 * This class is responsible for handling all business logic related to users.
 * 
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */
@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;

	private static Logger logger = LogManager.getLogger(UserService.class);

	public UserService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	/**
	 * Registers a new user or logs a warning if the user already exists.
	 * <p>
	 * This method checks if a user with the same username already exists in the repository. If no existing user is found, the new user is saved to the repository, and an informational message is logged to indicate successful registration. If a user with the given username already exists, a warning is logged, and no further action is taken. This process ensures that user data is not duplicated in the database.
	 *
	 * @param user The User object to be registered in the system.
	 * @return void This method does not return a value but logs the outcome of the registration attempt.
	 * @throws DataAccessException If database access fails or there are issues during the save operation.
	 * @see UserRepo#findByUsername(String) Method to check if a user already exists based on the username.
	 * @see UserRepo#save(User) Method to save a new user into the database.
	 */
	public void persist(User user) {
		Optional<User> returnedUser = userRepo.findByUsername(user.getUsername());
		if (returnedUser.isEmpty()) {
			userRepo.save(user);
			logger.info("User successfully registered");
		} else {
			logger.warn("User already exists");
		}
	}

	/**
	 * Updates an existing user's information in the database.
	 * <p>
	 * This method first checks if a user with the specified username exists in the repository. If the user is not found, a warning is logged indicating that the user does not exist. If the user exists, their information is updated in the database and a confirmation message is logged. This ensures that only existing users are updated, preventing the creation of duplicate entries.
	 *
	 * @param user The User object containing updated information to be saved.
	 * @return void This method does not return a value but logs the outcome of the update operation.
	 * @throws DataAccessException If there are issues accessing the database or saving the updated user data.
	 * @see UserRepo#findByUsername(String) Method to verify existence of the user based on username.
	 * @see UserRepo#save(User) Method to save the updated user data into the database.
	 */
	public void update(User user) {
		Optional<User> returnedUser = userRepo.findByUsername(user.getUsername());
		if (returnedUser.isEmpty()) {
			logger.warn("User does not exist in database");
		} else {
			userRepo.save(user);
			logger.info("User successfully updated");
		}
	}

	/**
	 * Retrieves a user's details based on their unique identifier.
	 * <p>
	 * This method attempts to find a user in the repository using the provided customer ID. If the user exists, their details are returned and an informational message is logged. If no user is found with the given ID, a warning is logged and null is returned. This method is useful for verification and retrieval of user-specific data within the system.
	 *
	 * @param customerId The unique identifier for the user to be retrieved.
	 * @return User object if found, otherwise null if no user matches the given ID.
	 * @throws DataAccessException If there are issues accessing the database or retrieving the user data.
	 * @see UserRepo#findById(long) Method used to fetch user details by ID.
	 */
	public User findUserById(long customerId) {
		Optional<User> returnedUser = userRepo.findById(customerId);
		if (returnedUser.isEmpty()) {
			logger.warn("Could not find User in Database");
			return null;
		} else {
			logger.info("Returning user's details");
			return returnedUser.get();
		}
	}

	/**
	 * Retrieves a user's details based on their username.
	 * <p>
	 * This method searches the repository for a user with the specified username. If a matching user is found, their details are returned, accompanied by an informational log message. If no user with the specified username exists, a warning is logged and the method returns null. This is useful for identity verification processes and user data retrieval operations.
	 *
	 * @param username The username of the user to be retrieved.
	 * @return User object containing the user's details if found; null if no user with the specified username exists.
	 * @throws DataAccessException If there are issues accessing the database or retrieving the user data.
	 * @see UserRepo#findByUsername(String) Method used to locate the user by username.
	 */
	public User findUserByUsername(String username) {
		Optional<User> returnedUser = userRepo.findByUsername(username);
		if (returnedUser.isEmpty()) {
			logger.warn("Could not find User in Database");
			return null;
		} else {
			logger.info("Returning user's details");
			return returnedUser.get();
		}
	}

	/**
	 * Deletes a user from the database by their unique identifier.
	 * <p>
	 * This method checks if a user exists in the database with the given identifier. If the user is found, they are removed from the database and a confirmation log message is recorded. If no user is found with the identifier, a warning is logged to indicate their absence. This method is utilized for managing user data, ensuring only existing records are deleted.
	 *
	 * @param userId The unique identifier of the user to be deleted.
	 * @return void This method does not return a value, but logs the outcome of the deletion process.
	 * @throws DataAccessException If there are issues accessing the database or performing the delete operation.
	 * @see UserRepo#findById(long) Method to check if a user exists based on the user ID.
	 * @see UserRepo#deleteById(long) Method to remove the user from the database.
	 */
	public void deleteById(long userId) {
		Optional<User> returnedUser = userRepo.findById(userId);
		if (returnedUser.isEmpty()) {
			logger.warn("User does not exist in database");
		} else {
			userRepo.deleteById(userId);
			logger.info("User deleted from Database");
		}
	}

	/**
	 * Retrieves all users from the database.
	 * <p>
	 * This method fetches a complete list of users stored in the user repository. It is used to obtain an overview of all registered users, which can be helpful for administrative purposes, reporting, or user management. The returned list may vary in size, depending on the number of users currently stored in the database.
	 *
	 * @return List of User objects representing all users in the database; this list can be empty if no users are stored.
	 * @throws DataAccessException If there are issues accessing the database or retrieving the user data.
	 * @see UserRepo#findAll() Method to fetch all records from the user repository.
	 */
	public List<User> findAllUsers() {
		List<User> users = userRepo.findAll();
		return users;
	}
	
}
