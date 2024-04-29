/**
 * 
 * CustomerService.java
 * - This file represents the service class of the Customer entity
 * 
 * @author Bryant Pang
 * @version 0.0.1
 * @since 22/03/24
 * 
 */
package com.fdmgroup.parceltracking.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.parceltracking.model.Customer;
import com.fdmgroup.parceltracking.repository.CustomerRepository;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository customerRepo;

	private static Logger logger = LogManager.getLogger(CustomerService.class);

	/**
	 * 
	 * Custom constructor for CustomerService class
	 * 
	 * @param customerRepo Customer repository class
	 */
	public CustomerService(CustomerRepository customerRepo) {
		this.customerRepo = customerRepo;
	}

	/**
	 * 
	 * This method helps sync data from Customer with the database
	 * 
	 * @param customer Customer that would have their data synced with the database
	 */
	public void persist(Customer customer) {
		Optional<Customer> returnedCustomer = customerRepo.findByUsername(customer.getUsername());
		if (returnedCustomer.isEmpty()) {
			customerRepo.save(customer);
			logger.info("Customer successfully registered");
		} else {
			logger.warn("Customer already exists");
		}
	}

	/**
	 * 
	 * This method returns a boolean value that indicates whether the customer is in
	 * the database
	 * 
	 * @param username Username of the Customer
	 * @return Boolean that represents whether the customer is in the database
	 */
	public boolean usernameInDatabase(String username) {
		Optional<Customer> returnedCustomer = customerRepo.findByUsername(username);
		if (returnedCustomer.isEmpty()) {
			logger.info("Username does not exist in database");
			return false;
		} else {
			logger.info("Username exists in database");
			return true;
		}
	}

	/**
	 * 
	 * This method returns a boolean value that indicates whether the login details
	 * are correct
	 * 
	 * @param username Username of the Customer
	 * @param password Password of the Customer
	 * @return Boolean that represents whether the login details are correct
	 */
	public boolean passwordChecker(String username, String password) {
		Optional<Customer> returnedCustomer = customerRepo.findByUsername(username);
		if (returnedCustomer.isEmpty()) {
			logger.warn("Username does not exist in database");
			return false;
		} else if (returnedCustomer.get().getPassword().equals(password)) {
			logger.info("Username and Password validated");
			return true;
		} else {
			logger.warn("Username and Password incorrect");
			return false;
		}
	}

	/**
	 * 
	 * This method helps Customers retrieve detailed information of their parcels
	 * based on the username of the Customer
	 * 
	 * @param username Username of the Customer
	 */
	public void findParcelsByUsername(String username) {
		Optional<Customer> returnedCustomer = customerRepo.findByUsername(username);

		if (returnedCustomer.isEmpty()) {
			logger.warn("Could not find Customer's details");
		} else {
			returnedCustomer.get().getParcelDetails();
			logger.info("Returning customer's parcel details");
		}
	}

	/**
	 * 
	 * This method returns the Customer based on the Customer's id
	 * 
	 * @param customerId Id of the Customer
	 * @return Customer that is represented by the Customer's id
	 */
	public Customer findCustomerById(long customerId) {
		Optional<Customer> returnedCustomer = customerRepo.findById(customerId);
		if (returnedCustomer.isEmpty()) {
			logger.warn("Could not find Customer in Database");
			return null;
		} else {
			logger.info("Returning customer's details");
			return returnedCustomer.get();
		}
	}

	/**
	 * 
	 * The method returns the Customer based on the Customer's username
	 * 
	 * @param username Username of the Customer
	 * @return Customer that is represented by the Customer's username
	 */
	public Customer findCustomerByUsername(String username) {
		Optional<Customer> returnedCustomer = customerRepo.findByUsername(username);
		if (returnedCustomer.isEmpty()) {
			logger.warn("Could not find Customer in Database");
			return null;
		} else {
			logger.info("Returning customer's details");
			return returnedCustomer.get();
		}
	}

	/**
	 * 
	 * This method helps Customers update their particulars and sync the information
	 * with the database
	 * 
	 * @param customer Customer object that would be used to update the current
	 *                 Customer's details
	 */
	public void update(Customer customer) {
		Optional<Customer> returnedCustomer = customerRepo.findByUsername(customer.getUsername());

		if (returnedCustomer.isEmpty()) {
			logger.warn("Customer does not exist in database");
		} else {
			customerRepo.save(customer);
			logger.info("Customer successfully updated");
		}
	}

	/**
	 * This method helps to Customers delete their accounts and remove their
	 * particulars from the database based on their id
	 * 
	 * @param id ID of the Customer
	 */
	public void deleteById(long id) {
		Optional<Customer> returnedCustomer = customerRepo.findById(id);

		if (returnedCustomer.isEmpty()) {
			logger.warn("Customer does not exist in database");
		} else {
			customerRepo.deleteById(id);
			logger.info("Customer deleted from Database");
		}
	}
}
