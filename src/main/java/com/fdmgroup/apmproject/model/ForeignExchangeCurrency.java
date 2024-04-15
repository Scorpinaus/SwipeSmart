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
@Table(name = "listOfForeignCurrencies")
public class ForeignExchangeCurrency {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Currency ID")
	private int currencyId;
	@Column(name = "Currency Name")
	private String currencyName;
	@Column(name = "Currency Value")
	private int currencyValue;

	@OneToMany(mappedBy = "transactionCurrency", fetch = FetchType.EAGER)
	private List<Transaction> transactions = new ArrayList<>();
	
	public ForeignExchangeCurrency() {};
	
	public ForeignExchangeCurrency(String currencyName, int currencyValue) {
		setCurrencyName(currencyName);
		setCurrencyValue(currencyValue);
	}

	public int getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public int getCurrencyValue() {
		return currencyValue;
	}

	public void setCurrencyValue(int currencyValue) {
		this.currencyValue = currencyValue;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Transaction transaction) {
		this.transactions.add(transaction);
	}
}
