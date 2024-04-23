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

/**
 * This class represents a foreign exchange currency in the banking system.
 *
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */
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
	
	/**
     * Constructs a new ForeignExchangeCurrency object with the specified details.
     */
	public ForeignExchangeCurrency() {};
	
	 /**
     * Constructs a new ForeignExchangeCurrency object with the specified details.
     *
     * @param code        The currency code.
     * @param alphaCode   The alpha code of the currency.
     * @param numericCode The numeric code of the currency.
     * @param currencyName The name of the currency.
     * @param currencyRate The exchange rate of the currency.
     * @param date        The date of the exchange rate.
     * @param inverseRate The inverse exchange rate of the currency.
     */
	public ForeignExchangeCurrency(String code, String alphaCode, String numericCode, String currencyName, double currencyRate, String date, double inverseRate) {
		setCode(code);
		setAlphaCode(alphaCode);
		setNumericCode(numericCode);
		setName(currencyName);
		setRate(currencyRate);
		setDate(date);
		setInverseRate(inverseRate);
	}
	
	/**
     * Returns the currency code.
     *
     * @return The currency code.
     */
    public String getCode() {
        return currencyCode;
    }

    /**
     * Sets the currency code.
     *
     * @param currencyCode The currency code to set.
     */
    public void setCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * Returns the inverse exchange rate of the currency.
     *
     * @return The inverse exchange rate.
     */
    public double getInverseRate() {
        return currencyInverseValue;
    }

    /**
     * Sets the inverse exchange rate of the currency.
     *
     * @param currencyInverseValue The inverse exchange rate to set.
     */
    public void setInverseRate(double currencyInverseValue) {
        this.currencyInverseValue = currencyInverseValue;
    }

    /**
     * Returns the date of the exchange rate.
     *
     * @return The date of the exchange rate.
     */
    public String getDate() {
        return currencyDate;
    }

    /**
     * Sets the date of the exchange rate.
     *
     * @param currencyDate The date of the exchange rate to set.
     */
    public void setDate(String currencyDate) {
        this.currencyDate = currencyDate;
    }

    /**
     * Returns the alpha code of the currency.
     *
     * @return The alpha code of the currency.
     */
    public String getAlphaCode() {
        return currencyAlphaCode;
    }

    /**
     * Sets the alpha code of the currency.
     *
     * @param currencyAlphaCode The alpha code of the currency to set.
     */
    public void setAlphaCode(String currencyAlphaCode) {
        this.currencyAlphaCode = currencyAlphaCode;
    }

    /**
     * Returns the numeric code of the currency.
     *
     * @return The numeric code of the currency.
     */
    public String getNumericCode() {
        return currencyNumericCode;
    }

    /**
     * Sets the numeric code of the currency.
     *
     * @param currencyNumericCode The numeric code of the currency to set.
     */
    public void setNumericCode(String currencyNumericCode) {
        this.currencyNumericCode = currencyNumericCode;
    }

    /**
     * Returns the ID of the currency.
     *
     * @return The currency ID.
     */
    public int getCurrencyId() {
        return currencyId;
    }

    /**
     * Sets the ID of the currency.
     *
     * @param currencyId The currency ID to set.
     */
    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    /**
     * Returns the name of the currency.
     *
     * @return The currency name.
     */
    public String getName() {
        return currencyName;
    }

    /**
     * Sets the name of the currency.
     *
     * @param currencyName The currency name to set.
     */
    public void setName(String currencyName) {
        this.currencyName = currencyName;
    }

    /**
     * Returns the exchange rate of the currency.
     *
     * @return The exchange rate.
     */
    public double getRate() {
        return currencyValue;
    }

    /**
     * Sets the exchange rate of the currency.
     *
     * @param currencyValue The exchange rate to set.
     */
    public void setRate(double currencyValue) {
        this.currencyValue = currencyValue;
    }

    /**
     * Returns the list of transactions associated with the currency.
     *
     * @return The list of transactions.
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Sets the list of transactions associated with the currency.
     *
     * @param transaction The transaction to add.
     */
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

	@Override
	public String toString() {
		return "ForeignExchangeCurrency [currencyId=" + currencyId + ", currencyName=" + currencyName
				+ ", currencyValue=" + currencyValue + ", currencyInverseValue=" + currencyInverseValue
				+ ", currencyDate=" + currencyDate + ", currencyAlphaCode=" + currencyAlphaCode
				+ ", currencyNumericCode=" + currencyNumericCode + ", currencyCode=" + currencyCode + "]";
	}

	
	
	
}
