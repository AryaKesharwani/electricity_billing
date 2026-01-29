package com.electricitybilling.users.controller;

import com.electricitybilling.users.dto.CustomerNameResponse;
import com.electricitybilling.users.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/customers")
@RequiredArgsConstructor
public class CustomersInternalController {

    private final CustomerService customerService;

    @GetMapping("/customer-name")
    public ResponseEntity<CustomerNameResponse> getCustomerName(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String consumerId) {
        String name = null;
        if (email != null && !email.isBlank()) {
            name = customerService.getCustomerNameByEmail(email);
        }
        if (name == null && consumerId != null && !consumerId.isBlank()) {
            name = customerService.getCustomerNameByConsumerId(consumerId);
        }
        return ResponseEntity.ok(new CustomerNameResponse(name));
    }

    @GetMapping("/consumer/{consumerId}")
    public ResponseEntity<Void> existsByConsumerId(@PathVariable String consumerId) {
        if (customerService.existsByConsumerId(consumerId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
