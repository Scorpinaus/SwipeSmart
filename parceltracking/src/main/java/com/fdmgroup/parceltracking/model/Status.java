/**
 * 
 * Status.java
 * - This file represents the POJO of the Status entity, where the Status ID
 * is the primary key and statusName as an attribute
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
@Table(name = "StatusOfParcels")
public class Status {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Status ID")
	private int statusId;

	@Column(name = "Status")
	private String statusName;

	@OneToMany(mappedBy = "status", fetch = FetchType.EAGER)
	private List<Parcel> parcels = new ArrayList<>();

	/**
	 * 
	 * Custom constructor of the Status entity
	 * 
	 * @param statusName Name of the Status
	 */
	public Status(String statusName) {
		this.statusName = statusName;
	}

	/**
	 * 
	 * Default constructor of the Status entity
	 * 
	 */
	public Status() {

	}

	/**
	 * 
	 * Generic getter method that returns the ID of the Status
	 * 
	 * @return int that represents the ID of the Status
	 */
	public int getStatusId() {
		return statusId;
	}

	/**
	 * 
	 * Generic setter method that sets the ID of the Status object
	 * 
	 * @param statusId ID of the Status object
	 */
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	/**
	 * 
	 * Generic getter method that returns the name of the Status object
	 * 
	 * @return String that represents the name of the status object
	 */
	public String getStatusName() {
		return statusName;
	}

	/**
	 * 
	 * Generic setter method that sets the name of the Status object
	 * 
	 * @param statusName Name of the Status object
	 */
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	/**
	 * 
	 * Generic getter method that returns the list of parcels that is linked to the
	 * Status entity
	 * 
	 * @return List that represents the list of parcels that is linked to the Status
	 *         entity
	 */
	public List<Parcel> getParcels() {
		return parcels;
	}

	/**
	 * 
	 * Generic setter method that adds the parcels to the Status entity
	 * 
	 * @param parcel Parcel that is related to the Status entity
	 */
	public void setParcels(Parcel parcel) {
		this.parcels.add(parcel);
	}

	/**
	 * 
	 * This method updates the Status object using another Status object
	 * 
	 * @param status Status object that will be used to update the current Status
	 *               object
	 */
	public void updateDetails(Status status) {
		setStatusName(status.getStatusName());
	}
}
