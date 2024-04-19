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

	@Column(name = "Currency Code")
	private String currencyCode;

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

	public CreditCard() {
	};

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
	}

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

	// Add balance when transactions are made
	public void addTransaction(double amount) {
		setAmountUsed(amountUsed + amount);
	}

	public void addTransactionMonthly(double amount) {
		setMonthlyBalance(monthlyBalance + amount);
	}

	@Override
	public String toString() {
		return "CreditCard [creditCardId=" + creditCardId + ", creditCardNumber=" + creditCardNumber + ", pin=" + pin
				+ ", cardLimit=" + cardLimit + ", cardType=" + cardType + ", amountUsed=" + amountUsed
				+ ", monthlyBalance=" + monthlyBalance + ", creditCardStatus=" + creditCardStatus + ", transactions="
				+ transactions + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(amountUsed, cardLimit, cardType, creditCardId, creditCardNumber, creditCardStatus,
				monthlyBalance, pin, transactions);
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
				&& Double.doubleToLongBits(monthlyBalance) == Double.doubleToLongBits(other.monthlyBalance)
				&& Objects.equals(pin, other.pin) && Objects.equals(transactions, other.transactions);
	}

}
