package com.electricitybilling.controller;

import com.electricitybilling.dto.RegisterCustomerRequest;
import com.electricitybilling.dto.RegisterCustomerResponse;
import com.electricitybilling.service.CustomerService;
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
@RequestMapping("/api/customers")
@Tag(name = "Customer", description = "Customer management endpoints")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @Operation(
            summary = "Register a new customer",
            description = "Register a new customer in the Electricity Management System. Creates entries in both Customer and Login tables."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or email format"),
            @ApiResponse(responseCode = "409", description = "Email or Consumer ID already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterCustomerResponse> registerCustomer(
            @Valid @RequestBody RegisterCustomerRequest request) {
        
        RegisterCustomerResponse response = customerService.registerCustomer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
