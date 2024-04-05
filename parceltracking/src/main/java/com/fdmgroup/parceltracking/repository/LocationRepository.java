/**
 * 
 * LocationRepository.java
 * - This file represents the repository class of the Location entity and
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

import com.fdmgroup.parceltracking.model.Location;

public interface LocationRepository extends JpaRepository<Location, Integer>{
	
	/**
	 * 
	 * This method returns the Location via the help of a JPA query
	 * 
	 * @param country Country of the Location
	 * @param city City of the Location
	 * @return Location that is represented by the country and city, if in the database
	 */
	@Query("select l from Location l where l.country = :countryName and l.city = :cityName" )
	Optional<Location> findByCityAndCountry(@Param("countryName") String country, @Param("cityName") String city);

}
