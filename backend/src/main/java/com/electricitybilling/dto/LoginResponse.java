package com.electricitybilling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String email;
    private String userType;
    private String status;
    private String message;
    private String token;
    private LocalDateTime loginTime;
    private String customerName;

    public LoginResponse(String email, String userType, String status, String message, String token) {
        this(email, userType, status, message, token, null);
    }

    public LoginResponse(String email, String userType, String status, String message, String token, String customerName) {
        this.email = email;
        this.userType = userType;
        this.status = status;
        this.message = message;
        this.token = token;
        this.loginTime = LocalDateTime.now();
        this.customerName = customerName;
    }
}
