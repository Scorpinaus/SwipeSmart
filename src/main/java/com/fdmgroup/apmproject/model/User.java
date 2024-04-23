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

/**
 * This class represents a user in the banking system.
 *
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */
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
	
	/**
     * Constructs a new User object with the specified details.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     */

	public User(String username, String password) {
		setUsername(username);
		setPassword(password);
		setRole("ROLE_USER");
	}
	
	/**
     * Constructs a new User object with the specified details.
     *
     * @param username  The username of the user.
     * @param password  The password of the user.
     * @param address   The address of the user.
     * @param firstName The first name of the user.
     * @param lastName  The last name of the user.
     */
	public User(String username, String password, String address, String firstName, String lastName) {
		setUsername(username);
		setPassword(password);
		setAddress(address);
		setFirstName(firstName);
		setLastName(lastName);
		setRole("ROLE_USER");
	}

	/**
     * Constructs a new User object without any details.
     */
    public User() {

    }

    /**
     * Returns the ID of the user.
     *
     * @return The user ID.
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user.
     *
     * @param userId The user ID to set.
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * Returns the username of the user.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password of the user.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the address of the user.
     *
     * @return The address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the user.
     *
     * @param address The address to set.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns the first name of the user.
     *
     * @return The first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName The first name to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the user.
     *
     * @return The last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName The last name to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the role of the user.
     *
     * @return The role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role The role to set.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Returns the list of credit cards associated with the user.
     *
     * @return The list of credit cards.
     */
    public List<CreditCard> getCreditCards() {
        return creditCards;
    }

    /**
     * Sets the list of credit cards associated with the user.
     *
     * @param creditCard The credit card to add.
     */
    public void setCreditCards(CreditCard creditCard) {
        this.creditCards.add(creditCard);
    }

    /**
     * Sets the list of credit cards associated with the user.
     *
     * @param creditCards The list of credit cards to set.
     */
    public void setCreditCardList(List<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }

    /**
     * Returns the list of accounts associated with the user.
     *
     * @return The list of accounts.
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Sets the list of accounts associated with the user.
     *
     * @param account The account to add.
     */
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
