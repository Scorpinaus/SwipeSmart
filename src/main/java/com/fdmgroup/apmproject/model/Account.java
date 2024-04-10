package com.fdmgroup.apmproject.model;

import java.util.ArrayList;
import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "listOfAccounts")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Account ID")
	private long accountId;
	@Column(name = "Account Name")
	private String accountName;
	@Column(name = "Account Balance")
	private double balance;

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "FK User ID")
	private long accountUserId;

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "FK Status ID")
	private int accountStatusId;

	@OneToMany(mappedBy = "transactionAccountId", fetch = FetchType.EAGER)
	private List<Transaction> transactions = new ArrayList<>();

	public Account(String accountName, double balance) {
		setAccountName(accountName);
		setBalance(balance);
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public long getAccountUserId() {
		return accountUserId;
	}

	public void setAccountUserId(long accountUserId) {
		this.accountUserId = accountUserId;
	}

	public int getAccountStatusId() {
		return accountStatusId;
	}

	public void setAccountStatusId(int accountStatusId) {
		this.accountStatusId = accountStatusId;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Transaction transaction) {
		this.transactions.add(transaction);
	}
}
