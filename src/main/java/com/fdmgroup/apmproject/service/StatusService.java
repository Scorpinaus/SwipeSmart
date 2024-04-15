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
	
	public void persist(Status status) {
		Optional<Status> returnedStatus = statusRepo.findById(status.getStatusId());
		if (returnedStatus.isEmpty()) {
			statusRepo.save(status);
			logger.info("Status successfully created");
		} else {
			logger.warn("Status already exists");
		}
	}
	
	public void update(Status status) {
		Optional<Status> returnedStatus = statusRepo.findById(status.getStatusId());
		if (returnedStatus.isEmpty()) {
			logger.warn("Status does not exist in database");
		} else {
			statusRepo.save(status);
			logger.info("Status successfully updated");
		}
	}
	
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
