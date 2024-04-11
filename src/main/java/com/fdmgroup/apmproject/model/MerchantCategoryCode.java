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
@Table(name = "listOfMerchantCategoryCode")
public class MerchantCategoryCode {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Merchant Category Code ID")
	private int merchantCategoryCodeId;
	@Column(name = "Merchant Category Code Number")
	private String merchantCategoryCodeNumber;
	@Column(name = "Merchant Category")
	private String merchantCategory;

	@OneToMany(mappedBy = "transactionReward", fetch = FetchType.EAGER)
	private List<Transaction> transactions = new ArrayList<>();

	public MerchantCategoryCode(int merchantCategoryCodeId, String merchantCategoryCodeNumber, String merchantCategory) {
		setMerchantCategoryCodeId(merchantCategoryCodeId);
		setMerchantCategoryCodeNumber(merchantCategoryCodeNumber);
		setMerchantCategory(merchantCategory);
	}

	public int getMerchantCategoryCodeId() {
		return merchantCategoryCodeId;
	}

	public void setMerchantCategoryCodeId(int merchantCategoryCodeId) {
		this.merchantCategoryCodeId = merchantCategoryCodeId;
	}

	public String getMerchantCategoryCodeNumber() {
		return merchantCategoryCodeNumber;
	}

	public void setMerchantCategoryCodeNumber(String merchantCategoryCodeNumber) {
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
}
