/**
 * 
 * Location.java
 * - This file represents the POJO of the Location entity, where the Location ID
 * is the primary key while country and city are its attributes
 * 
 * @author Bryant Pang
 * @version 0.0.1
 * @since 22/03/24
 * 
 */
package com.fdmgroup.parceltracking.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "parcelLocation")
public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Location ID")
	private int locationId;

	@Column(name = "Country")
	private String country;

	@Column(name = "City")
	private String city;

	@OneToMany(mappedBy = "location", fetch = FetchType.EAGER)
	private List<Parcel> parcels = new ArrayList<>();

	/**
	 * 
	 * Custom constructor of the Location entity
	 * 
	 * @param country Stores a country for a Location object
	 * @param city    Stores a city for a Location object
	 */
	public Location(String country, String city) {
		this.country = country;
		this.city = city;
	}

	/**
	 * 
	 * Default constructor of the Location entity
	 * 
	 */
	public Location() {

	}

	/**
	 * 
	 * Generic getter method that returns the ID of the Location Object
	 * 
	 * @return
	 */
	public int getLocationId() {
		return locationId;
	}

	/**
	 * 
	 * Generic setter method that sets the ID of the Location object
	 * 
	 * @param locationId
	 */
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	/**
	 * 
	 * Generic getter method that returns the country of the Location Object
	 * 
	 * @return
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * 
	 * Generic setter method that sets the country of the Location Object
	 * 
	 * @param country
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * 
	 * Generic getter method that returns the city of the Location Object
	 * 
	 * @return
	 */
	public String getCity() {
		return city;
	}

	/**
	 * 
	 * Generic setter method that sets the country of the Location Object
	 * 
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 
	 * Generic getter method that returns the list of parcels that is linked to the
	 * Location entity
	 * 
	 * @return List that represents the list of parcels that is linked to the
	 *         Location entity
	 */
	public List<Parcel> getParcels() {
		return parcels;
	}

	/**
	 * 
	 * Generic setter method that adds the parcels to the Location entity
	 * 
	 * @param parcel Parcel that is related to the Location entity
	 */
	public void setParcels(Parcel parcel) {
		this.parcels.add(parcel);
	}

	/**
	 * 
	 * This method updates the country and city of the Location object with another
	 * Location object
	 * 
	 * @param location Location object that will be used to update the current
	 *                 Location object
	 */
	public void updateDetails(Location location) {
		setCountry(location.getCountry());
		setCity(location.getCity());
	}
}
