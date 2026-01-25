package com.electricitybilling.controller;

import com.electricitybilling.dto.RegisterAdminRequest;
import com.electricitybilling.dto.RegisterAdminResponse;
import com.electricitybilling.service.AdminService;
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
@RequestMapping("/api/admin")
@Tag(name = "Administrator", description = "Administrator management endpoints")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Operation(
            summary = "Register a new administrator",
            description = "Register a new administrator in the Electricity Management System. Creates an entry in the login table with ADMIN user type. " +
                    "The administrator email must be unique and not match any existing customer email."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Administrator registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or email format"),
            @ApiResponse(responseCode = "409", description = "Email already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterAdminResponse> registerAdmin(
            @Valid @RequestBody RegisterAdminRequest request) {
        
        RegisterAdminResponse response = adminService.registerAdmin(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
