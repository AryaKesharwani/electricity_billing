package com.electricitybilling.auth.controller;

import com.electricitybilling.auth.dto.RegisterAdminRequest;
import com.electricitybilling.auth.dto.RegisterAdminResponse;
import com.electricitybilling.auth.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminRegisterController {

    private final AdminService adminService;

    @PostMapping("/register")
    public ResponseEntity<RegisterAdminResponse> registerAdmin(@Valid @RequestBody RegisterAdminRequest request) {
        RegisterAdminResponse response = adminService.registerAdmin(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
