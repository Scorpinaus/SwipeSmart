/**
 * 
 * Customer.java
 * - This file represents the POJO of the Customer entity, where the customer ID
 * is the primary key and shares a Bidirectional OneToMany relationship with Parcel entity
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
@Table(name = "listOfCustomers")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Customer ID")
	private long customerId;

	@OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
	private List<Parcel> parcels = new ArrayList<>();

	@Column(name = "Address")
	private String address;

	@Column(name = "Username")
	private String username;

	@Column(name = "Password")
	private String password;

	@Column(name = "First Name")
	private String firstName;

	@Column(name = "Last Name")
	private String lastName;

	@Column(name = "Profile Picture URL")
	private String profilePictureURL;

	@Column(name = "Background Picture URL")
	private String backgroundPictureURL;

	/**
	 * 
	 * Custom constructor of Customer entity
	 * 
	 * @param address   Address of the Customer
	 * @param username  Username of the Customer
	 * @param password  Password of the Customer
	 * @param firstName First Name of the Customer
	 * @param lastName  Last Name of the Customer
	 */
	public Customer(String address, String username, String password, String firstName, String lastName,
			String profilePictureURL, String backgroundPictureURL) {
		this.setAddress(address);
		this.setUsername(username);
		this.setPassword(password);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setProfileUrl(profilePictureURL);
		this.setBackgroundUrl(backgroundPictureURL);
	}

	/**
	 * 
	 * Default constructor of the Customer entity
	 * 
	 */
	public Customer() {

	}

	/**
	 * 
	 * Generic getter method that returns the ID of the Customer
	 * 
	 * @return Long that represents the ID of the Customer
	 */
	public long getId() {
		return customerId;
	}

	/**
	 * 
	 * Generic setter method that set the ID of the Customer
	 * 
	 * @param customerId ID of the Customer
	 */
	public void setId(long customerId) {
		this.customerId = customerId;
	}

	/**
	 * 
	 * Generic getter method that returns the address of the Customer
	 * 
	 * @return String that represents the address of the Customer
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 
	 * Generic setter method that sets the address of the Customer
	 * 
	 * @param address Address of the Customer
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 
	 * Generic getter method that returns the username of the Customer
	 * 
	 * @return String that represents the username of the Customer
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 
	 * Generic setter method that sets the username of the Customer
	 * 
	 * @param username Username of the Customer
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 
	 * Generic getter method that returns the password of the Customer
	 * 
	 * @return String that represents the username of the Customer
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * Generic setter method that sets the password of the Customer
	 * 
	 * @param password Password of the Customer
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 
	 * Generic getter method that returns the First Name of the Customer
	 * 
	 * @return String that represents the First Name of the Customer
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * 
	 * Generic setter method that sets the First Name of the Customer
	 * 
	 * @param firstName First Name of the Customer
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * 
	 * Generic getter method that returns the Last Name of the Customer
	 * 
	 * @return String that represents the Last Name of the Customer
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * 
	 * Generic setter method that sets the Last Name of the Customer
	 * 
	 * @param lastName Last Name of the Customer
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * 
	 * Generic setter method that adds the parcels that the Customer has ordered to
	 * a list of parcels
	 * 
	 * @param parcel Parcel that the Customer has ordered
	 */
	public void setParcels(Parcel parcel) {
		this.parcels.add(parcel);
	}

	/**
	 * 
	 * Generic getter method that returns the list of parcels that the Customer had
	 * ordered
	 * 
	 * @return List that represents the list of parcels that the Customer had
	 *         ordered
	 */
	public List<Parcel> getParcels() {
		return parcels;
	}

	/**
	 * 
	 * This method returns the profile URL of the Customer
	 * 
	 * @return String that represents the Profile URL of the customer
	 */
	public String getProfileUrl() {
		return profilePictureURL;
	}

	/**
	 * 
	 * This method sets the Profile URL of the Customer
	 * 
	 * @param profilePictureURL String that represents the Profile URL to be set
	 */
	public void setProfileUrl(String profilePictureURL) {
		this.profilePictureURL = profilePictureURL;
	}

	/**
	 * 
	 * This method gets the URL of the Background Picture
	 * 
	 * @return String that represents the URL of the Background Picture
	 */
	public String getBackgroundUrl() {
		return backgroundPictureURL;
	}

	/**
	 * 
	 * This method sets the URL of the Background Picture
	 * 
	 * @param backgroundPictureURL String that represents the Background URL to be
	 *                             set
	 */
	public void setBackgroundUrl(String backgroundPictureURL) {
		this.backgroundPictureURL = backgroundPictureURL;
	}

	/**
	 * 
	 * This method prints the details of the parcel's current location and status
	 * 
	 */
	public void getParcelDetails() {
		for (Parcel parcel : parcels) {
			System.out.println(parcel.getLocationDetails());
			System.out.println(parcel.getStatusDetails());
		}
	}

	/**
	 * 
	 * This methods allows the Customer to update their address, username, password,
	 * first name and last name
	 * 
	 * @param customer Customer with the new address, username, password, first name
	 *                 and last name
	 */
	public void updateDetails(Customer customer) {
		setAddress(customer.getAddress());
		setUsername(customer.getUsername());
		setPassword(customer.getPassword());
		setFirstName(customer.getFirstName());
		setLastName(customer.getLastName());
	}
}
