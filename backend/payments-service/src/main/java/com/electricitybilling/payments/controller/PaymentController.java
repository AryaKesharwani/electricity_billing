package com.electricitybilling.payments.controller;

import com.electricitybilling.payments.dto.PayBillRequest;
import com.electricitybilling.payments.dto.PayBillResponse;
import com.electricitybilling.payments.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payBills")
    public ResponseEntity<PayBillResponse> payBill(@Valid @RequestBody PayBillRequest request) {
        PayBillResponse response = paymentService.payBill(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
