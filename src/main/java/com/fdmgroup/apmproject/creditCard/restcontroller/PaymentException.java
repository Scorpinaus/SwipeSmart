package com.fdmgroup.apmproject.creditCard.restcontroller;

public class PaymentException extends RuntimeException {

    public PaymentException(String message) {
        super(message);
    }
}
