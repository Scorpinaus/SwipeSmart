package com.fdmgroup.apmproject.creditCard.restcontroller;

public class PurchaseRequest {


	private String accountName;
	private String accountNumber;
	private String creditCardNumber;
	private double amount;
	private String pin;

	private String mcc;
	private String currency;
	private String description;

	
	
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}



	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}



	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "PurchaseRequest [accountName=" + accountName + ", accountNumber=" + accountNumber
				+ ", creditCardNumber=" + creditCardNumber + ", amount=" + amount + ", pin=" + pin
				+ ", mcc=" + mcc + ", currencyId=" + currency
				+ ", description=" + description + "]";
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	
	
	
	// Getters and setters
}
