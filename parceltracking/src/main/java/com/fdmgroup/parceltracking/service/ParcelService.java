/**
 * 
 * ParcelService.java
 * - This file represents the service class of the Parcel entity
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

import com.fdmgroup.parceltracking.model.Parcel;
import com.fdmgroup.parceltracking.repository.ParcelRepository;

@Service
public class ParcelService {

	@Autowired
	private ParcelRepository parcelRepo;

	private static Logger logger = LogManager.getLogger(ParcelService.class);

	/**
	 * 
	 * Custom constructor for ParcelService class
	 * 
	 * @param parcelRepo Parcel Repository class
	 */
	public ParcelService(ParcelRepository parcelRepo) {
		this.parcelRepo = parcelRepo;
	}

	/**
	 * 
	 * This method helps sync data from Status object with the database
	 * 
	 * @param status Status that would have their data synced with the database
	 */
	public void persist(Parcel parcel) {
		Optional<Parcel> returnedParcel = parcelRepo.findById(parcel.getParcelId());
		if (returnedParcel.isEmpty()) {
			parcelRepo.save(parcel);
			logger.info("Parcel successfully created");
		} else {
			logger.warn("Parcel already exists!");
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
	public void update(Parcel parcel) {
		Optional<Parcel> returnedParcel = parcelRepo.findById(parcel.getParcelId());
		if (returnedParcel.isEmpty()) {
			logger.warn("Parcel does not exist in database!");
		} else {
			parcelRepo.save(parcel);
			logger.info("Parcel successfully updated");
		}
	}

	/**
	 * 
	 * This method helps delete Status objects and remove their particulars from the
	 * database based on their id
	 * 
	 * @param id ID of the Status object
	 */
	public void deleteById(long id) {
		Optional<Parcel> returnedParcel = parcelRepo.findById(id);

		if (returnedParcel.isEmpty()) {
			logger.warn("Parcel does not exist in database!");
		} else {
			parcelRepo.deleteById(id);
			logger.info("Parcel deleted from Database");
		}
	}

	/**
	 * 
	 * This method returns the Parcel based on the id given
	 * 
	 * @param id Id of the Parcel
	 * @return Parcel based on the id given
	 */
	public Parcel findById(long id) {
		Optional<Parcel> returnedParcel = parcelRepo.findById(id);

		if (returnedParcel.isEmpty()) {
			logger.warn("Parcel retrieved from Database");
			return null;
		} else {
			logger.info("Parcel retrieved from Database");
			return returnedParcel.get();
		}
	}
}
