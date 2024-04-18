package com.fdmgroup.apmproject.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;

	private static Logger logger = LogManager.getLogger(UserService.class);

	public UserService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	public void persist(User user) {
		Optional<User> returnedUser = userRepo.findByUsername(user.getUsername());
		if (returnedUser.isEmpty()) {
			userRepo.save(user);
			logger.info("User successfully registered");
		} else {
			logger.warn("User already exists");
		}
	}

	public void update(User user) {
		Optional<User> returnedUser = userRepo.findByUsername(user.getUsername());
		if (returnedUser.isEmpty()) {
			logger.warn("User does not exist in database");
		} else {
			userRepo.save(user);
			logger.info("User successfully updated");
		}
	}

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

	public void deleteById(long userId) {
		Optional<User> returnedUser = userRepo.findById(userId);
		if (returnedUser.isEmpty()) {
			logger.warn("User does not exist in database");
		} else {
			userRepo.deleteById(userId);
			logger.info("User deleted from Database");
		}
	}

	public List<User> findAllUsers() {
		List<User> users = userRepo.findAll();
		return users;
	}

	@PostConstruct
	public void initUsers() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		User user = new User("jackytan", encoder.encode("Qwerty1"), "Sentosa", "Jacky", "Tan");
		User user2 = new User("admin", encoder.encode("Fdm123456"), "admin's house", "admin", "chan");
		user2.setRole("ROLE_ADMIN");
		persist(user);
		persist(user2);
	}
	
	
}
