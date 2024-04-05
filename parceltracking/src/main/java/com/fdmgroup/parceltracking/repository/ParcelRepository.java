/**
 * 
 * ParcelRepository.java
 * - This file represents the repository class of the Parcel entity and
 * extends JpaRepository to perform its tasks
 * 
 * @author Bryant Pang
 * @version 0.0.1
 * @since 02/04/24
 * 
 */
package com.fdmgroup.parceltracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.parceltracking.model.Parcel;

@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Long>{
	
}
