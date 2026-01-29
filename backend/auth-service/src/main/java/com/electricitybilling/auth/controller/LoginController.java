package com.electricitybilling.auth.controller;

import com.electricitybilling.auth.dto.LoginRequest;
import com.electricitybilling.auth.dto.LoginResponse;
import com.electricitybilling.auth.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/validateLogin")
    public ResponseEntity<LoginResponse> validateLogin(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = loginService.validateLogin(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
