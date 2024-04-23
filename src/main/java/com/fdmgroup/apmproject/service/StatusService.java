package com.fdmgroup.apmproject.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.apmproject.model.Status;
import com.fdmgroup.apmproject.repository.StatusRepository;

import jakarta.annotation.PostConstruct;

@Service
public class StatusService {
	@Autowired
	private StatusRepository statusRepo;
	
	private static Logger logger = LogManager.getLogger(StatusService.class);
	
	public StatusService(StatusRepository statusRepo) {
		this.statusRepo = statusRepo;
	}
	
	/**
	Persists a Status object into the database if it doesn't already exist.
	<p>
	This method checks if a Status object with the provided ID already exists in the database. If not, it saves the provided Status object into the repository using the StatusRepository. If a Status with the same ID already exists, it logs a warning message indicating that the Status already exists and does not perform any persistence operation.
	@param status The Status object to be persisted.
	@throws Exception if an error occurs during the persistence process.
	@see StatusRepository#findById(Object) findById
	@see StatusRepository#save(Object) save
	*/
	public void persist(Status status) {
		Optional<Status> returnedStatus = statusRepo.findById(status.getStatusId());
		if (returnedStatus.isEmpty()) {
			statusRepo.save(status);
			logger.info("Status successfully created");
		} else {
			logger.warn("Status already exists");
		}
	}
	
	/**
	Updates a Status object in the database if it already exists.
	<p>
	This method checks if a Status object with the provided ID already exists in the database using the StatusRepository. If the Status exists, it updates the Status object in the repository with the provided Status object. If the Status does not exist, it logs a warning message indicating that the Status does not exist in the database and does not perform any update operation.
	@param status The Status object to be updated.
	@throws Exception if an error occurs during the update process.
	@see StatusRepository#findById(Object) findById
	@see StatusRepository#save(Object) save
	*/
	public void update(Status status) {
		Optional<Status> returnedStatus = statusRepo.findById(status.getStatusId());
		if (returnedStatus.isEmpty()) {
			logger.warn("Status does not exist in database");
		} else {
			statusRepo.save(status);
			logger.info("Status successfully updated");
		}
	}
	
	/**
	Finds and retrieves a Status object by its ID from the database.
	<p>
	This method searches for a Status object in the database using the provided status ID. If the Status object with the given ID exists, it returns the corresponding Status object. If no Status object is found with the provided ID, it logs a warning message indicating that the Status could not be found in the database and returns null.
	@param statusId The ID of the Status object to be found.
	@return The Status object with the specified ID if found, otherwise null.
	@see StatusRepository#findById(Object) findById
	*/
	public Status findById(int statusId) {
		Optional<Status> returnedStatus = statusRepo.findById(statusId);
		if (returnedStatus.isEmpty()) {
			logger.warn("Could not find Status in Database");
			return null;
		} else {
			logger.info("Returning Status' details");
			return returnedStatus.get();
		}
	}
	
	public Status findByStatusName(String statusName) {
		Optional<Status> returnedStatus = statusRepo.findByStatusName(statusName);
		if (returnedStatus.isEmpty()) {
			logger.warn("Could not find Status in Database");
			return null;
		} else {
			logger.info("Returning Status' details");
			return returnedStatus.get();
		}
	}
	
	public void deleteById(int statusId) {
		Optional<Status> returnedStatus = statusRepo.findById(statusId);
		if (returnedStatus.isEmpty()) {
			logger.warn("Status does not exist in database");
		} else {
			statusRepo.deleteById(statusId);
			logger.info("Status deleted from Database");
		}
	}
	
	@PostConstruct
	public void initStatus() {
		Status status = new Status("Pending");
		Status status2 = new Status("Approved");
		Status status3 = new Status("Disabled");
		
		persist(status);
		persist(status2);
		persist(status3);
	}
}
