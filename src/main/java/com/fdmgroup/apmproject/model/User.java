package com.fdmgroup.apmproject.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
	@Column(name = "Role")
	private String role;

	@OneToMany(mappedBy = "creditCardUser", fetch = FetchType.EAGER)
	private List<CreditCard> creditCards = new ArrayList<>();

	@OneToMany(mappedBy = "accountUser", fetch = FetchType.EAGER)
	private List<Account> accounts = new ArrayList<>();
	
	public User(String username, String password) {
		setUsername(username);
		setPassword(password);
		setRole("ROLE_USER");
	}
	

	public User(String username, String password, String address, String firstName, String lastName) {
		setUsername(username);
		setPassword(password);
		setAddress(address);
		setFirstName(firstName);
		setLastName(lastName);
		setRole("ROLE_USER");
	}

	public User() {

	};

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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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

	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", address=" + address
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", role=" + role + ", creditCards="
				+ creditCards + ", accounts=" + accounts + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(accounts, address, creditCards, firstName, lastName, password, role, userId, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(accounts, other.accounts) && Objects.equals(address, other.address)
				&& Objects.equals(creditCards, other.creditCards) && Objects.equals(firstName, other.firstName)
				&& Objects.equals(lastName, other.lastName) && Objects.equals(password, other.password)
				&& Objects.equals(role, other.role) && userId == other.userId
				&& Objects.equals(username, other.username);
	}
	
	
	
}
