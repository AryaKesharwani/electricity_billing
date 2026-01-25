package com.electricitybilling.exception;

import lombok.Getter;

@Getter
public class BillAlreadyPaidException extends RuntimeException {
    
    public BillAlreadyPaidException(String message) {
        super(message);
    }
    
    public BillAlreadyPaidException(String message, Throwable cause) {
        super(message, cause);
    }
}
