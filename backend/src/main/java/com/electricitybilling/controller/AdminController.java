package com.electricitybilling.controller;

import com.electricitybilling.dto.CustomerListItem;
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

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administrator", description = "Administrator management endpoints")
@RequiredArgsConstructor
public class AdminController {

    private final CustomerService customerService;

    @Operation(summary = "List all customers", description = "Get all registered customers (admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of customers returned successfully")
    })
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerListItem>> listCustomers() {
        List<CustomerListItem> customers = customerService.findAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Create new consumer", description = "Register a new customer/consumer (admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Email or Consumer ID already exists")
    })
    @PostMapping("/customers")
    public ResponseEntity<RegisterCustomerResponse> createConsumer(
            @Valid @RequestBody RegisterCustomerRequest request) {
        RegisterCustomerResponse response = customerService.registerCustomer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete consumer", description = "Delete a customer and their bills, payments, and login (admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @DeleteMapping("/customers/{consumerId}")
    public ResponseEntity<Void> deleteConsumer(@PathVariable String consumerId) {
        customerService.deleteCustomer(consumerId);
        return ResponseEntity.noContent().build();
    }
}
