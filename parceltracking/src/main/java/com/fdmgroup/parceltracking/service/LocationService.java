/**
 * 
 * LocationService.java
 * - This file represents the service class of the Location entity
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

import com.fdmgroup.parceltracking.model.Location;
import com.fdmgroup.parceltracking.repository.LocationRepository;

@Service
public class LocationService {

	@Autowired
	private LocationRepository locationRepo;

	private static Logger logger = LogManager.getLogger(LocationService.class);

	/**
	 * 
	 * Custom constructor for LocationService class
	 * 
	 * @param locationRepo Location repository class
	 */
	public LocationService(LocationRepository locationRepo) {
		this.locationRepo = locationRepo;
	}

	/**
	 * 
	 * This method helps sync data from Location object with the database
	 * 
	 * @param location Location object that would have their data synced with the
	 *                 database
	 */
	public void persist(Location location) {
		Optional<Location> returnedLocation = locationRepo.findById(location.getLocationId());
		if (returnedLocation.isEmpty()) {
			locationRepo.save(location);
			logger.info("Location successfully created");
		} else {
			logger.warn("Location already exists!");
		}
	}

	/**
	 * 
	 * This method helps update the Location object's data and sync the information
	 * with the database
	 * 
	 * @param location Location object that would be used to update the current
	 *                 Location's details
	 */
	public void update(Location location) {
		Optional<Location> returnedLocation = locationRepo.findById(location.getLocationId());

		if (returnedLocation.isEmpty()) {
			logger.warn("Location does not exist in database!");
		} else {
			locationRepo.save(location);
			logger.info("Location successfully updated");
		}
	}

	/**
	 * 
	 * This method helps to delete Location objects and remove their particulars
	 * from the database based on their id
	 * 
	 * @param id ID of the Location
	 */
	public void deleteById(int id) {
		Optional<Location> returnedLocation = locationRepo.findById(id);

		if (returnedLocation.isEmpty()) {
			logger.warn("Location does not exist in database!");
		} else {
			locationRepo.deleteById(id);
			logger.info("Location deleted from Database");
		}
	}

	/**
	 * 
	 * This method returns the boolean value of whether the Location exists in the
	 * database
	 * 
	 * @param country Country of Location
	 * @param city    City of Location
	 * @return Boolean value that indicates whether the Location exists in the
	 *         database
	 */
	public boolean locationInDatabase(String country, String city) {
		Optional<Location> returnedLocation = locationRepo.findByCityAndCountry(country, city);
		if (returnedLocation.isEmpty()) {
			logger.info("Location does not exist in database");
			return false;
		} else {
			logger.info("Location exists in database");
			return true;
		}
	}

	/**
	 * 
	 * This method returns the Location based on the country and city
	 * 
	 * @param country Country of Location
	 * @param city    City of Location
	 * @return Location based on the country and city
	 */
	public Location findByLocation(String country, String city) {
		Optional<Location> returnedLocation = locationRepo.findByCityAndCountry(country, city);
		if (returnedLocation.isEmpty()) {
			logger.warn("Location does not exist in database");
			return null;
		} else {
			logger.info("Location retrieved");
			return returnedLocation.get();
		}
	}
}
