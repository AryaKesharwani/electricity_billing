package com.electricitybilling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCustomerResponse {

    private String consumerId;
    private String customerName;
    private String email;
    private String message;
    private LocalDateTime registeredAt;

    public RegisterCustomerResponse(String consumerId, String customerName, String email, String message) {
        this.consumerId = consumerId;
        this.customerName = customerName;
        this.email = email;
        this.message = message;
        this.registeredAt = LocalDateTime.now();
    }
}
