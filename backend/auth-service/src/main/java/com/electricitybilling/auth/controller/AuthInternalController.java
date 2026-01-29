package com.electricitybilling.auth.controller;

import com.electricitybilling.auth.dto.CreateLoginRequest;
import com.electricitybilling.auth.dto.RegisterAdminRequest;
import com.electricitybilling.auth.dto.RegisterAdminResponse;
import com.electricitybilling.auth.service.AdminService;
import com.electricitybilling.auth.service.AuthInternalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/auth")
@RequiredArgsConstructor
public class AuthInternalController {

    private final AuthInternalService authInternalService;
    private final AdminService adminService;

    @PostMapping("/create-login")
    public ResponseEntity<Void> createLogin(@Valid @RequestBody CreateLoginRequest request) {
        authInternalService.createLogin(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/login-by-consumer/{consumerId}")
    public ResponseEntity<Void> deleteLoginByConsumerId(@PathVariable String consumerId) {
        authInternalService.deleteLoginByConsumerId(consumerId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/login-by-email")
    public ResponseEntity<Void> deleteLoginByEmail(@RequestParam String email) {
        authInternalService.deleteLoginByEmail(email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register-admin")
    public ResponseEntity<RegisterAdminResponse> registerAdmin(@Valid @RequestBody RegisterAdminRequest request) {
        RegisterAdminResponse response = adminService.registerAdmin(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
