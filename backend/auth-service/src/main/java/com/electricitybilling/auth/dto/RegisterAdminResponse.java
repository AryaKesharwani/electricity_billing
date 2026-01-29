package com.electricitybilling.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterAdminResponse {

    private String email;
    private String userType;
    private String status;
    private String message;
    private LocalDateTime registeredAt;

    public RegisterAdminResponse(String email, String userType, String status, String message) {
        this.email = email;
        this.userType = userType;
        this.status = status;
        this.message = message;
        this.registeredAt = LocalDateTime.now();
    }
}
