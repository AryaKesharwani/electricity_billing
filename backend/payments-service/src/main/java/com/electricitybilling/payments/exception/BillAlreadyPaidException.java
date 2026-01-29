package com.electricitybilling.payments.exception;

public class BillAlreadyPaidException extends RuntimeException {

    public BillAlreadyPaidException(String message) {
        super(message);
    }
}
