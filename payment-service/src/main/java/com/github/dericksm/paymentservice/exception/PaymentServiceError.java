package com.github.dericksm.paymentservice.exception;


public class PaymentServiceError extends RuntimeException {

    public PaymentServiceError(String orderId) {
        super(String.format("Could not process order with id: %s with reason: insufficient balance", orderId));
    }
}
