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
@Table(name = "listOfCreditCards")
public class CreditCard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Credit Card ID")
	private long creditCardId;
	@Column(name = "Credit Card Number")
	private String creditCardNumber;
	@Column(name = "CVC/CVV")
	private String pin;
	@Column(name = "Credit Limit")
	private int cardLimit;
	@Column(name = "Card Type")
	private String cardType;

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "FK User ID")
	private long creditCardUserId;

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "FK Status ID")
	private int creditCardStatusId;

	@OneToMany(mappedBy = "transactionCreditCardId", fetch = FetchType.EAGER)
	private List<Transaction> transactions = new ArrayList<>();

	public CreditCard(String creditCardNumber, String pin, int cardLimit, String cardType) {
		setCreditCardNumber(creditCardNumber);
		setPin(pin);
		setCardLimit(cardLimit);
		setCardType(cardType);
	}

	public long getCreditCard() {
		return creditCardId;
	}

	public void setCreditCard(long creditCardId) {
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

	public int getCardLimit() {
		return cardLimit;
	}

	public void setCardLimit(int cardLimit) {
		this.cardLimit = cardLimit;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public long getCreditCardUserId() {
		return creditCardUserId;
	}

	public void setCreditCardUserId(long creditCardUserId) {
		this.creditCardUserId = creditCardUserId;
	}

	public int getCreditCardStatusId() {
		return creditCardStatusId;
	}

	public void setCreditCardStatusId(int creditCardStatusId) {
		this.creditCardStatusId = creditCardStatusId;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Transaction transaction) {
		this.transactions.add(transaction);
	}
}
