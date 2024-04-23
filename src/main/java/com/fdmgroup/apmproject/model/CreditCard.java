package com.fdmgroup.apmproject.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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



/**
 * This class represents a credit card in the banking system.
 *
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */
@Entity
@Table(name = "listOfCreditCards")
public class CreditCard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Credit Card ID")
	private long creditCardId;
	@Column(name = "Credit Card Number", unique = true)
	private String creditCardNumber;
	@Column(name = "CVC/CVV")
	private String pin;
	@Column(name = "Credit Limit")
	private double cardLimit;
	@Column(name = "Card Type")
	private String cardType;
	@Column(name = "Amount Used")
	private double amountUsed;
	@Column(name = "Monthly Balance")
	private double monthlyBalance;
	
	@Column(name = "Interest")
	private double interest;

	@Column(name = "Currency Code")
	private String currencyCode;
	
	@Column(name = "Mininum Balance Paid?")
	private double minBalancePaid;

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "FK User ID")
	private User creditCardUser;

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "FK Status ID")
	private Status creditCardStatus;

	@OneToMany(mappedBy = "transactionCreditCard", fetch = FetchType.EAGER)
	private List<Transaction> transactions = new ArrayList<>();
	
	/**
     * Constructs a new CreditCard object with the specified details.
     */
	public CreditCard() {
	};

	/**
     * Constructs a new CreditCard object with the specified details.
     *
     * @param creditCardNumber The credit card number.
     * @param pin              The PIN for the credit card.
     * @param cardLimit        The credit limit for the card.
     * @param cardType         The type of credit card.
     * @param status           The status of the credit card.
     * @param amountUsed       The amount of credit used.
     * @param creditCardUser   The user associated with the credit card.
     * @param currencyCode     The currency code of the credit card.
     */
	public CreditCard(String creditCardNumber, String pin, double cardLimit, String cardType, Status status,
			double amountUsed, User creditCardUser, String currencyCode) {
		setCreditCardNumber(creditCardNumber);
		setPin(pin);
		setCardLimit(cardLimit);
		setCardType(cardType);
		setCreditCardStatus(status);
		setCreditCardUser(creditCardUser);
		setAmountUsed(amountUsed);
		setMonthlyBalance(0);
		setCurrencyCode(currencyCode);
		setMinBalancePaid(50);
	}
	
	/**
     * Constructs a new CreditCard object with the specified details.
     *
     * @param creditCardNumber The credit card number.
     * @param pin              The PIN for the credit card.
     * @param cardLimit        The credit limit for the card.
     * @param cardType         The type of credit card.
     * @param status           The status of the credit card.
     * @param amountUsed       The amount of credit used.
     * @param creditCardUser   The user associated with the credit card.
     */
	public CreditCard(String creditCardNumber, String pin, double cardLimit, String cardType, Status status,
			double amountUsed, User creditCardUser) {
		setCreditCardNumber(creditCardNumber);
		setPin(pin);
		setCardLimit(cardLimit);
		setCardType(cardType);
		setCreditCardStatus(status);
		setCreditCardUser(creditCardUser);
		setAmountUsed(amountUsed);
		setMonthlyBalance(0);
		setMinBalancePaid(50);
	}

	public long getCreditCardId() {
		return creditCardId;
	}

	public void setCreditCardId(long creditCardId) {
		this.creditCardId = creditCardId;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public double getCardLimit() {
		return cardLimit;
	}

	public void setCardLimit(double cardLimit) {
		this.cardLimit = cardLimit;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public double getAmountUsed() {
		return amountUsed;
	}

	public void setAmountUsed(double amountUsed) {
		this.amountUsed = amountUsed;
	}

	public User getCreditCardUser() {
		return creditCardUser;
	}

	public void setCreditCardUserId(User creditCardUser) {
		this.creditCardUser = creditCardUser;
	}

	public Status getCreditCardStatus() {
		return creditCardStatus;
	}

	public void setCreditCardStatus(Status creditCardStatus) {
		this.creditCardStatus = creditCardStatus;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Transaction transaction) {
		this.transactions.add(transaction);
	}

	public void setCreditCardUser(User creditCardUser) {
		this.creditCardUser = creditCardUser;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public double getMonthlyBalance() {
		return monthlyBalance;
	}

	public void setMonthlyBalance(double monthlyBalance) {
		this.monthlyBalance = monthlyBalance;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
		
	
	public double getMinBalancePaid() {
		return minBalancePaid;
	}

	public void setMinBalancePaid(double d) {
		this.minBalancePaid = d;
	}

	// Add balance when transactions are made
	public void addTransaction(double amount) {
		setAmountUsed(amountUsed + amount);
	}

	public void addTransactionMonthly(double amount) {
		setMonthlyBalance(monthlyBalance + amount);
	}

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	@Override
	public String toString() {
		return "CreditCard [creditCardId=" + creditCardId + ", creditCardNumber=" + creditCardNumber + ", pin=" + pin
				+ ", cardLimit=" + cardLimit + ", cardType=" + cardType + ", amountUsed=" + amountUsed
				+ ", monthlyBalance=" + monthlyBalance + ", interest=" + interest + ", currencyCode=" + currencyCode
				+ ", minBalancePaid=" + minBalancePaid + ", creditCardStatus=" + creditCardStatus + ", transactions="
				+ transactions + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(amountUsed, cardLimit, cardType, creditCardId, creditCardNumber, creditCardStatus,
				currencyCode, interest, minBalancePaid, monthlyBalance, pin, transactions);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreditCard other = (CreditCard) obj;
		return Double.doubleToLongBits(amountUsed) == Double.doubleToLongBits(other.amountUsed)
				&& Double.doubleToLongBits(cardLimit) == Double.doubleToLongBits(other.cardLimit)
				&& Objects.equals(cardType, other.cardType) && creditCardId == other.creditCardId
				&& Objects.equals(creditCardNumber, other.creditCardNumber)
				&& Objects.equals(creditCardStatus, other.creditCardStatus)
				&& Objects.equals(currencyCode, other.currencyCode)
				&& Double.doubleToLongBits(interest) == Double.doubleToLongBits(other.interest)
				&& minBalancePaid == other.minBalancePaid
				&& Double.doubleToLongBits(monthlyBalance) == Double.doubleToLongBits(other.monthlyBalance)
				&& Objects.equals(pin, other.pin) && Objects.equals(transactions, other.transactions);
	}
	
	
	

}
