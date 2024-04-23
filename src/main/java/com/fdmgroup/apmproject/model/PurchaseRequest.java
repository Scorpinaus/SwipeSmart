package com.fdmgroup.apmproject.model;

/**
 * This class represents a purchase request for a credit card.
 *
 * @author Bard
 * @version 1.0
 * @since 2024-04-22
 */
public class PurchaseRequest {

	private String accountName;
	private String accountNumber;
	private String creditCardNumber;
	private double amount;
	private String pin;

	private String mcc;
	private String currency;
	private String description;

	/**
	 * Constructs a new PurchaseRequest object with the specified details.
	 *
	 * @param accountName      The name of the account to be debited.
	 * @param accountNumber    The account number to be debited.
	 * @param creditCardNumber The credit card number to be used for the purchase.
	 * @param amount           The amount of the purchase.
	 * @param pin              The PIN of the credit card.
	 * @param mcc              The merchant category code of the purchase.
	 * @param currency         The currency of the purchase.
	 * @param description      The description of the purchase.
	 */
	public PurchaseRequest(String accountName, String accountNumber, String creditCardNumber, double amount, String pin,
			String mcc, String currency, String description) {
		this.accountName = accountName;
		this.accountNumber = accountNumber;
		this.creditCardNumber = creditCardNumber;
		this.amount = amount;
		this.pin = pin;
		this.mcc = mcc;
		this.currency = currency;
		this.description = description;
	}

	/**
	 * Returns the name of the account to be debited.
	 *
	 * @return The account name.
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * Sets the name of the account to be debited.
	 *
	 * @param accountName The account name to set.
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * Returns the account number to be debited.
	 *
	 * @return The account number.
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * Sets the account number to be debited.
	 *
	 * @param accountNumber The account number to set.
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * Returns the credit card number to be used for the purchase.
	 *
	 * @return The credit card number.
	 */
	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	/**
	 * Sets the credit card number to be used for the purchase.
	 *
	 * @param creditCardNumber The credit card number to set.
	 */
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	/**
	 * Returns the amount of the purchase.
	 *
	 * @return The purchase amount.
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * Sets the amount of the purchase.
	 *
	 * @param amount The purchase amount to set.
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * Returns the PIN of the credit card.
	 *
	 * @return The credit card PIN.
	 */
	public String getPin() {
		return pin;
	}

	/**
	 * Sets the PIN of the credit card.
	 *
	 * @param pin The credit card PIN to set.
	 */
	public void setPin(String pin) {
		this.pin = pin;
	}

	/**
	 * Returns the merchant category code of the purchase.
	 *
	 * @return The merchant category code.
	 */
	public String getMcc() {
		return mcc;
	}

	/**
	 * Sets the merchant category code of the purchase.
	 *
	 * @param mcc The merchant category code to set.
	 */
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	/**
	 * Returns the currency of the purchase.
	 *
	 * @return The purchase currency.
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * Sets the currency of the purchase.
	 *
	 * @param currency The purchase currency to set.
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * Returns the description of the purchase.
	 *
	 * @return The purchase description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the purchase.
	 *
	 * @param description The purchase description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
    public String toString() {
        return "PurchaseRequest{" +
                "accountName='" + accountName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", creditCardNumber='" + creditCardNumber + '\'' +
                ", amount=" + amount +
                ", pin='" + pin + '\'' +
                ", mcc='" + mcc + '\'' +
                ", currency='" + currency + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
