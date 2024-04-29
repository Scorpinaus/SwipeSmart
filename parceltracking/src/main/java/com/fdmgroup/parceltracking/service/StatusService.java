/**
 * 
 * StatusService.java
 * - This file represents the service class of the Status entity
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

import com.fdmgroup.parceltracking.model.Status;
import com.fdmgroup.parceltracking.repository.StatusRepository;

@Service
public class StatusService {

	@Autowired
	private StatusRepository statusRepo;

	private static Logger logger = LogManager.getLogger(StatusService.class);

	/**
	 * 
	 * Custom constructor for StatusService class
	 * 
	 * @param statusRepo Status Repository class
	 */
	public StatusService(StatusRepository statusRepo) {
		this.statusRepo = statusRepo;
	}

	/**
	 * 
	 * This method helps sync data from Status object with the database
	 * 
	 * @param status Status that would have their data synced with the database
	 */
	public void persist(Status status) {
		Optional<Status> returnedStatus = statusRepo.findById(status.getStatusId());
		if (returnedStatus.isEmpty()) {
			statusRepo.save(status);
			logger.info("Status successfully created");
		} else {
			logger.warn("Status already exists!");
		}
	}

	/**
	 * 
	 * This method helps update the details of the Status object and sync the
	 * information with the database
	 * 
	 * @param status Status object that would be used to update the current Status'
	 *               details
	 */
	public void update(Status status) {
		Optional<Status> returnedStatus = statusRepo.findById(status.getStatusId());

		if (returnedStatus.isEmpty()) {
			logger.warn("Status does not exist in database!");
		} else {
			statusRepo.save(status);
			logger.info("Status successfully updated");
		}
	}

	/**
	 * 
	 * This method helps delete Status objects and remove their particulars from the
	 * database based on their id
	 * 
	 * @param id ID of the Status object
	 */
	public void deleteById(int id) {
		Optional<Status> returnedStatus = statusRepo.findById(id);
		if (returnedStatus.isEmpty()) {
			logger.warn("Status does not exist in database!");
		} else {
			statusRepo.deleteById(id);
			logger.info("Status deleted from Database");
		}
	}

	/**
	 * 
	 * This method returns the boolean value that indicates if the status exists in
	 * the database
	 * 
	 * @param statusName Name of the Status
	 * @return Boolean that indicates if the status exists
	 */
	public boolean statusInDatabase(String statusName) {
		Optional<Status> returnedStatus = statusRepo.findByStatus(statusName);
		if (returnedStatus.isEmpty()) {
			logger.info("Status does not exist in database");
			return false;
		} else {
			logger.info("Status exists in database");
			return true;
		}
	}

	/**
	 * 
	 * This method returns the Status based on the name provided
	 * 
	 * @param statusName Name of the Status
	 * @return Status based on the name provided
	 */
	public Status findByStatus(String statusName) {
		Optional<Status> returnedStatus = statusRepo.findByStatus(statusName);
		if (returnedStatus.isEmpty()) {
			logger.info("Status does not exist in database");
			return null;
		} else {
			logger.info("Status retrieved from Database");
			return returnedStatus.get();
		}
	}
}
