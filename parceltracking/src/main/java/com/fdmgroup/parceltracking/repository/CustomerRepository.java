/**
 * 
 * CustomerRepository.java
 * - This file represents the repository class of the Customer entity and
 * extends JpaRepository to perform its tasks
 * 
 * @author Bryant Pang
 * @version 0.0.1
 * @since 02/04/24
 * 
 */
package com.fdmgroup.parceltracking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fdmgroup.parceltracking.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
	/**
	 * 
	 * This method returns the Customer via the help of a JPA query
	 * 
	 * @param username Username of the Customer
	 * @return Customer that represents the username of the Customer, if in the database
	 */
	@Query("select c from Customer c where c.username = :customerUsername")
	Optional<Customer> findByUsername(@Param("customerUsername") String username);
}
