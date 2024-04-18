package com.fdmgroup.apmproject.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForeignExchangeCurrency {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Currency ID")
	private int currencyId;
	@Column(name = "Currency Name")
	private String currencyName;
	@Column(name = "Currency Rate")
	private double currencyValue;
	@Column(name = "Inverse Currency Rate")
	private double currencyInverseValue;
	@Column(name = "Currency Date")
	private String currencyDate;
	@Column(name = "Currency Alpha Code")
	private String currencyAlphaCode;
	@Column(name = "Currency Numeric Code")
	private String currencyNumericCode;
	@Column(name = "Currency Code")
	private String currencyCode;
	
	@JsonIgnore
	@OneToMany(mappedBy = "transactionCurrency", fetch = FetchType.EAGER)
	private List<Transaction> transactions = new ArrayList<>();
	
	public ForeignExchangeCurrency() {};
	
	public ForeignExchangeCurrency(String code, String alphaCode, String numericCode, String currencyName, double currencyRate, String date, double inverseRate) {
		setCode(code);
		setAlphaCode(alphaCode);
		setNumericCode(numericCode);
		setName(currencyName);
		setRate(currencyRate);
		setDate(date);
		setInverseRate(inverseRate);
	}
	
	public String getCode() {
		return currencyCode;
	}

	public void setCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public double getInverseRate() {
		return currencyInverseValue;
	}

	public void setInverseRate(double currencyInverseValue) {
		this.currencyInverseValue = currencyInverseValue;
	}

	public String getDate() {
		return currencyDate;
	}

	public void setDate(String currencyDate) {
		this.currencyDate = currencyDate;
	}

	public String getAlphaCode() {
		return currencyAlphaCode;
	}

	public void setAlphaCode(String currencyAlphaCode) {
		this.currencyAlphaCode = currencyAlphaCode;
	}

	public String getNumericCode() {
		return currencyNumericCode;
	}

	public void setNumericCode(String currencyNumericCode) {
		this.currencyNumericCode = currencyNumericCode;
	}

	public int getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}

	public String getName() {
		return currencyName;
	}

	public void setName(String currencyName) {
		this.currencyName = currencyName;
	}

	public double getRate() {
		return currencyValue;
	}

	public void setRate(double currencyValue) {
		this.currencyValue = currencyValue;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Transaction transaction) {
		this.transactions.add(transaction);
	}

	@Override
	public int hashCode() {
		return Objects.hash(currencyAlphaCode, currencyDate, currencyId, currencyInverseValue, currencyName,
				currencyNumericCode, currencyValue);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ForeignExchangeCurrency other = (ForeignExchangeCurrency) obj;
		return Objects.equals(currencyAlphaCode, other.currencyAlphaCode)
				&& Objects.equals(currencyDate, other.currencyDate) && currencyId == other.currencyId
				&& Double.doubleToLongBits(currencyInverseValue) == Double.doubleToLongBits(other.currencyInverseValue)
				&& Objects.equals(currencyName, other.currencyName)
				&& Objects.equals(currencyNumericCode, other.currencyNumericCode)
				&& Double.doubleToLongBits(currencyValue) == Double.doubleToLongBits(other.currencyValue);
	}

	
	
	
}
