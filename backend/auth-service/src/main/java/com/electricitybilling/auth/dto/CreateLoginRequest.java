package com.electricitybilling.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLoginRequest {

    private String consumerId;
    private String email;
    private String userId;
    private String password;
    private String userType; // CUSTOMER or ADMIN
}
