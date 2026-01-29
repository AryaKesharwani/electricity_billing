package com.electricitybilling.payments.controller;

import com.electricitybilling.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/payments")
@RequiredArgsConstructor
public class PaymentsInternalController {

    private final PaymentService paymentService;

    @DeleteMapping("/consumer/{consumerId}")
    public ResponseEntity<Void> deletePaymentsByConsumerId(@PathVariable String consumerId) {
        paymentService.deletePaymentsByConsumerId(consumerId);
        return ResponseEntity.noContent().build();
    }
}
