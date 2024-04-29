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
 * This class represents a status in the banking system.
 *
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */
@Entity
@Table(name = "listOfStatuses")
public class Status {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Status ID")
	private int statusId;
	@Column(name = "Status Name",nullable = false,unique = true)
	private String statusName;

	@OneToMany(mappedBy = "accountStatus", fetch = FetchType.EAGER)
	private List<Account> accounts = new ArrayList<>();

	@OneToMany(mappedBy = "creditCardStatus", fetch = FetchType.EAGER)
	private List<CreditCard> creditCards = new ArrayList<>();
	
	/**
     * Constructs a new Status object with the specified details.
     */
    public Status() {
    }

    /**
     * Constructs a new Status object with the specified details.
     *
     * @param statusName The name of the status.
     */
    public Status(String statusName) {
        setStatusName(statusName);
    }

    /**
     * Returns the ID of the status.
     *
     * @return The status ID.
     */
    public int getStatusId() {
        return statusId;
    }

    /**
     * Sets the ID of the status.
     *
     * @param statusId The status ID to set.
     */
    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    /**
     * Returns the name of the status.
     *
     * @return The status name.
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * Sets the name of the status.
     *
     * @param statusName The status name to set.
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    /**
     * Returns the list of accounts associated with the status.
     *
     * @return The list of accounts.
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Sets the list of accounts associated with the status.
     *
     * @param account The account to add.
     */
    public void setAccounts(Account account) {
        this.accounts.add(account);
    }

    /**
     * Returns the list of credit cards associated with the status.
     *
     * @return The list of credit cards.
     */
    public List<CreditCard> getCreditCards() {
        return creditCards;
    }

    /**
     * Sets the list of credit cards associated with the status.
     *
     * @param creditCard The credit card to add.
     */
    public void setCreditCards(CreditCard creditCard) {
        this.creditCards.add(creditCard);
    }
	@Override
	public String toString() {
		return "Status [statusId=" + statusId + ", statusName=" + statusName + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(statusId, statusName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Status other = (Status) obj;
		return statusId == other.statusId && Objects.equals(statusName, other.statusName);
	}
	
	
}
