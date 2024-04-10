package com.fdmgroup.apmproject.model;

import java.util.Date;

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
import jakarta.persistence.Table;

@Entity
@Table(name = "listOfTransactions")
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Transaction ID")
	private long transactionId;
	@Column(name = "Merchant Category Code")
	private String merchantCategoryCode;
	@Column(name = "Transaction Date")
	private Date transactionDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "FK Credit Card ID")
	private long transactionCreditCardId;

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "FK Account ID")
	private long transactionAccountId;

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "FK Reward ID")
	private Reward transactionReward;

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "FK Foreign Exchange Currency ID")
	private ForeignExchangeCurrency transactionCurrency;

	public Transaction(String merchantCategoryCode, Date transactionDate) {
		setMerchantCategoryCode(merchantCategoryCode);
		setTransactionDate(transactionDate);
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public String getMerchantCategoryCode() {
		return merchantCategoryCode;
	}

	public void setMerchantCategoryCode(String merchantCategoryCode) {
		this.merchantCategoryCode = merchantCategoryCode;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public long getTransactionCreditCardId() {
		return transactionCreditCardId;
	}

	public void setTransactionCreditCardId(long transactionCreditCardId) {
		this.transactionCreditCardId = transactionCreditCardId;
	}

	public long getTransactionAccountId() {
		return transactionAccountId;
	}

	public void setTransactionAccountId(long transactionAccountId) {
		this.transactionAccountId = transactionAccountId;
	}

	public Reward getTransactionReward() {
		return transactionReward;
	}

	public void setTransactionReward(Reward transactionReward) {
		this.transactionReward = transactionReward;
	}

	public ForeignExchangeCurrency getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(ForeignExchangeCurrency transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

}
