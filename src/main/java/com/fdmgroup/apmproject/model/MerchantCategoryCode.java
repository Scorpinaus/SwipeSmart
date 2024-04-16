package com.fdmgroup.apmproject.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "listOfMerchantCategoryCode")
public class MerchantCategoryCode {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Merchant Category Code ID")
	private int merchantCategoryCodeId;
	@Column(name = "Merchant Category Code Number", unique = true)
	private int merchantCategoryCodeNumber;
	@Column(name = "Merchant Category", unique = true)
	private String merchantCategory;

	@OneToMany(mappedBy = "transactionMerchantCategoryCode", fetch = FetchType.EAGER)
	private List<Transaction> transactions = new ArrayList<>();
	
	public MerchantCategoryCode() {};
	
	public MerchantCategoryCode(int merchantCategoryCodeNumber, String merchantCategory) {
		setMerchantCategoryCodeNumber(merchantCategoryCodeNumber);
		setMerchantCategory(merchantCategory);
	}
	
	
	// getters and setters
	public int getMerchantCategoryCodeId() {
		return merchantCategoryCodeId;
	}

	public void setMerchantCategoryCodeId(int merchantCategoryCodeId) {
		this.merchantCategoryCodeId = merchantCategoryCodeId;
	}

	public int getMerchantCategoryCodeNumber() {
		return merchantCategoryCodeNumber;
	}

	public void setMerchantCategoryCodeNumber(int merchantCategoryCodeNumber) {
		this.merchantCategoryCodeNumber = merchantCategoryCodeNumber;
	}

	public String getMerchantCategory() {
		return merchantCategory;
	}

	public void setMerchantCategory(String merchantCategory) {
		this.merchantCategory = merchantCategory;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Transaction transaction) {
		this.transactions.add(transaction);
	}
	
	// Override 
	@Override
	public String toString() {
		return "MerchantCategoryCode [merchantCategoryCodeId=" + merchantCategoryCodeId
				+ ", merchantCategoryCodeNumber=" + merchantCategoryCodeNumber + ", merchantCategory="
				+ merchantCategory + ", transactions=" + transactions + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(merchantCategory, merchantCategoryCodeId, merchantCategoryCodeNumber, transactions);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MerchantCategoryCode other = (MerchantCategoryCode) obj;
		return Objects.equals(merchantCategory, other.merchantCategory)
				&& merchantCategoryCodeId == other.merchantCategoryCodeId
				&& Objects.equals(merchantCategoryCodeNumber, other.merchantCategoryCodeNumber)
				&& Objects.equals(transactions, other.transactions);
	}
	
	
}
