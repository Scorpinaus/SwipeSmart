/**
 * 
 * StatusRepository.java
 * - This file represents the repository class of the Status entity and
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

import com.fdmgroup.parceltracking.model.Status;

public interface StatusRepository extends JpaRepository<Status, Integer>{
	
	/**
	 * 
	 * This method returns the Status via the help of a JPA query
	 * 
	 * @param status Name of the Status
	 * @return Status that is represented by the status name, if in the database
	 */
	@Query(value="select * from status_of_parcels s where s.status = :statusName",nativeQuery = true)
	Optional<Status> findByStatus(@Param("statusName") String status);

	
}
