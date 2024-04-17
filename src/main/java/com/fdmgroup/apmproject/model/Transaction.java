package com.fdmgroup.apmproject.model;

import java.time.LocalDateTime;
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
import jakarta.persistence.Table;

@Entity
@Table(name = "listOfTransactions")
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Transaction ID")
	private long transactionId;
	@Column(name = "Transaction Date")
	private LocalDateTime transactionDate;
	@Column(name = "Transaction Type")
	private String transactionType;
	@Column(name = "Transaction Amount")
	private double transactionAmount;
	@Column(name = "Recipient Account Number")
	private String recipientAccountNumber;
	@Column(name = "Cashback")
	private double cashback;

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "FK Credit Card ID")
	private CreditCard transactionCreditCard;

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "FK Account ID")
	private Account transactionAccount;
	
	//example
	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "FK recipientAccount ID")
	private Account recipientAccount;

	public Account getRecipientAccount() {
		return recipientAccount;
	}

	public void setRecipientAccount(Account recipientAccount) {
		this.recipientAccount = recipientAccount;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "FK Merchant Category Code ID")
	private MerchantCategoryCode transactionMerchantCategoryCode;

	@ManyToOne(fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "FK Foreign Exchange Currency ID")
	private ForeignExchangeCurrency transactionCurrency;
	
	public Transaction() {};

	public Transaction(LocalDateTime transactionDate, String transactionType,
			double transactionAmount, String recipientAccountNumber, double cashback, CreditCard transactionCreditCard,
			Account transactionAccount, MerchantCategoryCode mcc,
			ForeignExchangeCurrency transactionCurrency) {
		setTransactionDate(transactionDate);
		setTransactionType(transactionType);
		setTransactionAmount(transactionAmount);
		setRecipientAccountNumber(recipientAccountNumber);
		setCashback(cashback);
		setTransactionCreditCard(transactionCreditCard);
		setTransactionAccount(transactionAccount);
		setTransactionMerchantCategoryCode(mcc);
		setTransactionCurrency(transactionCurrency);
	}
	
	public Transaction(String transactionType,
			double transactionAmount, String recipientAccountNumber, double cashback, CreditCard transactionCreditCard,
			Account transactionAccount, MerchantCategoryCode mcc,
			ForeignExchangeCurrency transactionCurrency) {
		setTransactionDate(LocalDateTime.now());
		setTransactionType(transactionType);
		setTransactionAmount(transactionAmount);
		setRecipientAccountNumber(recipientAccountNumber);
		setCashback(cashback);
		setTransactionCreditCard(transactionCreditCard);
		setTransactionAccount(transactionAccount);
		setTransactionMerchantCategoryCode(mcc);
		setTransactionCurrency(transactionCurrency);
	}
	//record transaction for bank account
	public Transaction(String transactionType, Account transactionAccount, double transactionAmount,String recipientAccountNumber, ForeignExchangeCurrency transactionCurrency
			) {
		setTransactionDate(LocalDateTime.now());
		
		setTransactionType(transactionType);
		
		setTransactionAccount(transactionAccount);
		
		setTransactionAmount(transactionAmount);
		
		setRecipientAccountNumber(recipientAccountNumber);
		
		setTransactionCurrency(transactionCurrency);
	}

	public Transaction(String transactionType, Account transactionAccount,Account recipientAccount, double transactionAmount,String recipientAccountNumber, ForeignExchangeCurrency transactionCurrency
			) {
		setTransactionDate(LocalDateTime.now());
		
		setTransactionType(transactionType);
		
		setTransactionAccount(transactionAccount);
		
		setTransactionAmount(transactionAmount);
		
		setRecipientAccountNumber(recipientAccountNumber);
		
		setTransactionCurrency(transactionCurrency);
		
		setRecipientAccount(recipientAccount);
	}
	
	
	
	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	
	public String getRecipientAccountNumber() {
		return recipientAccountNumber;
	}

	public void setRecipientAccountNumber(String recipientAccountNumber) {
		this.recipientAccountNumber = recipientAccountNumber;
	}
	
	public double getCashback() {
		return cashback;
	}

	public void setCashback(double cashback) {
		this.cashback = cashback;
	}
	
	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	public CreditCard getTransactionCreditCard() {
		return transactionCreditCard;
	}

	public void setTransactionCreditCard(CreditCard transactionCreditCard) {
		this.transactionCreditCard = transactionCreditCard;
	}

	public Account getTransactionAccount() {
		return transactionAccount;
	}

	public void setTransactionAccount(Account transactionAccount) {
		this.transactionAccount = transactionAccount;
	}

	public ForeignExchangeCurrency getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(ForeignExchangeCurrency transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}


	public MerchantCategoryCode getTransactionMerchantCategoryCode() {
		return transactionMerchantCategoryCode;
	}

	public void setTransactionMerchantCategoryCode(MerchantCategoryCode transactionMerchantCategoryCode) {
		this.transactionMerchantCategoryCode = transactionMerchantCategoryCode;
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", transactionDate=" + transactionDate
				+ ", transactionType=" + transactionType + ", cashback=" + cashback
				+ ", transactionMerchantCategoryCode=" + transactionMerchantCategoryCode.getMerchantCategory() + ", transactionCurrency="
				+ transactionCurrency + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(cashback, recipientAccountNumber, transactionAmount, transactionCurrency, transactionDate,
				transactionId, transactionMerchantCategoryCode, transactionType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		return Double.doubleToLongBits(cashback) == Double.doubleToLongBits(other.cashback)
				&& Objects.equals(recipientAccountNumber, other.recipientAccountNumber)
				&& Double.doubleToLongBits(transactionAmount) == Double.doubleToLongBits(other.transactionAmount)
				&& Objects.equals(transactionCurrency, other.transactionCurrency)
				&& Objects.equals(transactionDate, other.transactionDate) && transactionId == other.transactionId
				&& Objects.equals(transactionMerchantCategoryCode, other.transactionMerchantCategoryCode)
				&& Objects.equals(transactionType, other.transactionType);
	}
	
	
	
	
}
