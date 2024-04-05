/**
 * 
 * Parcel.java
 * - This file represents the POJO of the Parcel entity, where the Parcel ID
 * is the primary key and shares a Bidirectional ManyToOne relationship with 
 * Customer entity and a ManyToOne Unidirectional relationship with Location and 
 * Status entities
 * 
 * @author Bryant Pang
 * @version 0.0.1
 * @since 22/03/24
 * 
 */
package com.fdmgroup.parceltracking.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "listOfParcels")
public class Parcel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Order ID")
	private long parcelId;

	@Column(name = "Shipping Address")
	private String shippingAddress;

	@Column(name = "Name")
	private String name;

	@Column(name = "Picture URL")
	private String pictureURL;

	@Column(name = "Credit Card Number")
	private String creditCardNumber;

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "FK Customer ID")
	private Customer customer;

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "FK Location ID")
	private Location location;

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "FK Status ID")
	private Status status;

	/**
	 * 
	 * Custom constructor of the Parcel entity
	 * 
	 * @param customer Customer that ordered the parcel
	 * @param location Location of the parcel
	 * @param status   Status of the parcel
	 */
	public Parcel(Customer customer, Location location, Status status, String name, String pictureURL) {
		this.customer = customer;
		customer.setParcels(this);
		this.setLocation(location);
		this.setStatus(status);
		this.setName(name);
		this.setPictureURL(pictureURL);
	}

	/**
	 * 
	 * Default constructor of the Parcel entity
	 * 
	 */
	public Parcel() {
		return;
	}

	/**
	 * 
	 * Generic getter method that returns the ID of the Parcel
	 * 
	 * @return Long that represents the ID of the Parcel
	 */
	public long getParcelId() {
		return parcelId;
	}

	/**
	 * 
	 * Generic setter method that sets the ID of the Parcel
	 * 
	 * @param parcelId ID of the Parcel
	 */
	public void setParcelId(long parcelId) {
		this.parcelId = parcelId;
	}

	/**
	 * 
	 * Generic getter method that returns the location of the Parcel
	 * 
	 * @return Location that represents the location of the Parcel
	 */
	public Location getLocation() {
		return this.location;
	}

	/**
	 * 
	 * Generic setter method that sets the location of the Parcel
	 * 
	 * @param location Location of the Parcel
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * 
	 * Generic getter method that returns the status of the Parcel
	 * 
	 * @return Status that represents the status of the Parcel
	 */
	public Status getStatus() {
		return this.status;
	}

	/**
	 * 
	 * Generic setter method that sets the status of the Parcel
	 * 
	 * @param status Status of the Parcel
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * 
	 * This method returns the full details of the status of the Parcel
	 * 
	 * @return String that represents the full details of the status of the Parcel
	 */
	public String getStatusDetails() {
		return "Parcel " + Long.toString(parcelId) + " is currently " + status.getStatusName();
	}

	/**
	 * 
	 * This method returns the shipping address of the Parcel
	 * 
	 * @return String that represents the shipping address of the Parcel
	 */
	public String getShippingAddress() {
		return shippingAddress;
	}

	/**
	 * 
	 * This method sets the shipping address of the Parcel
	 * 
	 * @param shippingAddress Shipping Address of the Parcel
	 */
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	/**
	 * 
	 * This method returns the name of the Parcel
	 * 
	 * @return String that represents the name of the Parcel
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * This method sets the name of the Parcel
	 * 
	 * @param name Name of the Parcel
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * This method returns the picture URL of the Parcel
	 * 
	 * @return String that represents URL of the Parcel
	 */
	public String getPictureURL() {
		return pictureURL;
	}

	/**
	 * 
	 * This method sets the picture URL of the Parcel
	 * 
	 * @param pictureURL Picture URL of the Parcel
	 */
	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}

	/**
	 * 
	 * This method returns the credit card number used to purchase the Parcel
	 * 
	 * @return String that represents the credit card number used to purchase the
	 *         Parcel
	 */
	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	/**
	 * 
	 * This method sets the credit card number used to purchase the Parcel
	 * 
	 * @param creditCardNumber Credit Card Number used to purchase the Parcel
	 */
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	/**
	 * 
	 * This method returns the full details of the location of the Parcel
	 * 
	 * @return String that represents the full details of the location of the Parcel
	 */
	public String getLocationDetails() {
		return "Parcel " + Long.toString(parcelId) + " is currently in " + location.getCity() + ", "
				+ location.getCountry();
	}

	/**
	 * 
	 * This method updates the location and status of the Parcel
	 * 
	 * @param parcel New Status and Location of the Parcel
	 */
	public void updateDetails(Parcel parcel) {
		setLocation(parcel.getLocation());
		setStatus(parcel.getStatus());
	}

}
