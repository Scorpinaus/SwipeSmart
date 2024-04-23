package com.fdmgroup.apmproject.model;

/**
 * This class represents a custom exception that is thrown when a payment error
 * occurs.
 *
 * @author
 * @version 1.0
 * @since 2024-04-22
 */
public class PaymentException extends RuntimeException {
	/**
	 * Constructs a new PaymentException with the specified message.
	 *
	 * @param message The message to be associated with the exception.
	 */
	public PaymentException(String message) {
		super(message);
	}
}
