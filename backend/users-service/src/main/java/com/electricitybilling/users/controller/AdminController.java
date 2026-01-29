package com.electricitybilling.users.controller;

import com.electricitybilling.users.dto.CustomerListItem;
import com.electricitybilling.users.dto.RegisterCustomerRequest;
import com.electricitybilling.users.dto.RegisterCustomerResponse;
import com.electricitybilling.users.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CustomerService customerService;

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerListItem>> listCustomers() {
        List<CustomerListItem> customers = customerService.findAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @PostMapping("/customers")
    public ResponseEntity<RegisterCustomerResponse> createConsumer(@Valid @RequestBody RegisterCustomerRequest request) {
        RegisterCustomerResponse response = customerService.registerCustomer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/customers/{consumerId}")
    public ResponseEntity<Void> deleteConsumer(@PathVariable String consumerId) {
        customerService.deleteCustomer(consumerId);
        return ResponseEntity.noContent().build();
    }
}
