package com.electricitybilling.users.controller;

import com.electricitybilling.users.dto.RegisterCustomerRequest;
import com.electricitybilling.users.dto.RegisterCustomerResponse;
import com.electricitybilling.users.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<RegisterCustomerResponse> registerCustomer(@Valid @RequestBody RegisterCustomerRequest request) {
        RegisterCustomerResponse response = customerService.registerCustomer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
