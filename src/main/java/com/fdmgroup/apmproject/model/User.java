package com.fdmgroup.apmproject.model;

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
@Table(name = "listOfUsers")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Customer ID")
	private long userId;
	@Column(name = "Username")
	private String username;
	@Column(name = "Password")
	private String password;
	@Column(name = "Address")
	private String address;
	@Column(name = "First Name")
	private String firstName;
	@Column(name = "Last Name")
	private String lastName;

	@OneToMany(mappedBy = "creditCardUser", fetch = FetchType.EAGER)
	private List<CreditCard> creditCards = new ArrayList<>();

	@OneToMany(mappedBy = "accountUser", fetch = FetchType.EAGER)
	private List<Account> accounts = new ArrayList<>();

	public User(String username, String password, String address, String firstName, String lastName) {
		setUsername(username);
		setPassword(password);
		setAddress(address);
		setFirstName(firstName);
		setLastName(lastName);
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<CreditCard> getCreditCards() {
		return creditCards;
	}

	public void setCreditCards(CreditCard creditCard) {
		this.creditCards.add(creditCard);
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Account account) {
		this.accounts.add(account);
	}
}
