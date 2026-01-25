package com.electricitybilling.controller;

import com.electricitybilling.dto.LoginRequest;
import com.electricitybilling.dto.LoginResponse;
import com.electricitybilling.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User authentication endpoints")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @Operation(
            summary = "Validate user login",
            description = "Validate user credentials (userName and password) and return login information. " +
                    "The userName can be email, userId, or consumerId. " +
                    "Login will be denied if the account is deactivated."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "403", description = "Account is deactivated"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/validateLogin")
    public ResponseEntity<LoginResponse> validateLogin(
            @Valid @RequestBody LoginRequest request) {
        
        LoginResponse response = loginService.validateLogin(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
