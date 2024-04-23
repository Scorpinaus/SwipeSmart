package com.fdmgroup.apmproject.creditCard.restcontroller;

/**
 * This class represents a response object for payment operations.
 *
 * @author
 * @version 1.0
 * @since 2024-04-22
 */
public class PaymentResponse {
	private boolean success;
	private String message;

	/**
	 * Constructs a new PaymentResponse object with the specified success status and
	 * message.
	 *
	 * @param success The success status of the payment operation.
	 * @param message The message associated with the payment operation.
	 */
	public PaymentResponse(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	/**
	 * Returns the success status of the payment operation.
	 *
	 * @return True if the payment operation was successful, false otherwise.
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * Sets the success status of the payment operation.
	 *
	 * @param success The success status to set.
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * Returns the message associated with the payment operation.
	 *
	 * @return The message associated with the payment operation.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message associated with the payment operation.
	 *
	 * @param message The message to set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
