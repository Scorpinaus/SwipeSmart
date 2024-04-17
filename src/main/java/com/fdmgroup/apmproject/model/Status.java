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
@Table(name = "listOfStatuses")
public class Status {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Status ID")
	private int statusId;
	@Column(name = "Status Name")
	private String statusName;

	@OneToMany(mappedBy = "accountStatus", fetch = FetchType.EAGER)
	private List<Account> accounts = new ArrayList<>();

	@OneToMany(mappedBy = "creditCardStatus", fetch = FetchType.EAGER)
	private List<CreditCard> creditCards = new ArrayList<>();
	
	public Status() {} ;
	
	public Status(String statusName) {
		setStatusName(statusName);
	}
	
	// getters and setters
	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Account account) {
		this.accounts.add(account);
	}

	public List<CreditCard> getCreditCards() {
		return creditCards;
	}

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
