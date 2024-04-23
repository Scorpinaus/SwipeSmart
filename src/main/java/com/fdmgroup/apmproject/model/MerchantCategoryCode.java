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
/**
 * This class represents a merchant category code (MCC) in the banking system.
*
* @author 
* @version 1.0
* @since 2024-04-22
*/
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
	
	/**
     * Constructs a new MerchantCategoryCode object with the specified details.
     */
	public MerchantCategoryCode() {};
	
	/**
     * Constructs a new MerchantCategoryCode object with the specified details.
     *
     * @param merchantCategoryCodeNumber The merchant category code number.
     * @param merchantCategory           The merchant category.
     */
	public MerchantCategoryCode(int merchantCategoryCodeNumber, String merchantCategory) {
		setMerchantCategoryCodeNumber(merchantCategoryCodeNumber);
		setMerchantCategory(merchantCategory);
	}
	
	
	/**
     * Returns the ID of the merchant category code.
     *
     * @return The merchant category code ID.
     */
    public int getMerchantCategoryCodeId() {
        return merchantCategoryCodeId;
    }

    /**
     * Sets the ID of the merchant category code.
     *
     * @param merchantCategoryCodeId The merchant category code ID to set.
     */
    public void setMerchantCategoryCodeId(int merchantCategoryCodeId) {
        this.merchantCategoryCodeId = merchantCategoryCodeId;
    }

    /**
     * Returns the merchant category code number.
     *
     * @return The merchant category code number.
     */
    public int getMerchantCategoryCodeNumber() {
        return merchantCategoryCodeNumber;
    }

    /**
     * Sets the merchant category code number.
     *
     * @param merchantCategoryCodeNumber The merchant category code number to set.
     */
    public void setMerchantCategoryCodeNumber(int merchantCategoryCodeNumber) {
        this.merchantCategoryCodeNumber = merchantCategoryCodeNumber;
    }

    /**
     * Returns the merchant category.
     *
     * @return The merchant category.
     */
    public String getMerchantCategory() {
        return merchantCategory;
    }

    /**
     * Sets the merchant category.
     *
     * @param merchantCategory The merchant category to set.
     */
    public void setMerchantCategory(String merchantCategory) {
        this.merchantCategory = merchantCategory;
    }

    /**
     * Returns the list of transactions associated with the merchant category code.
     *
     * @return The list of transactions.
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Sets the list of transactions associated with the merchant category code.
     *
     * @param transaction The transaction to add.
     */
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
