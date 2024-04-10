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
@Table(name = "listOfStatuses")
public class Status {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Status ID")
	private int statusId;
	@Column(name = "Status Name")
	private String statusName;

	@OneToMany(mappedBy = "accountStatusId", fetch = FetchType.EAGER)
	private List<Account> accounts = new ArrayList<>();

	@OneToMany(mappedBy = "creditCardStatusId", fetch = FetchType.EAGER)
	private List<CreditCard> creditCards = new ArrayList<>();

	public Status(String statusName) {
		setStatusName(statusName);
	}

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
}
