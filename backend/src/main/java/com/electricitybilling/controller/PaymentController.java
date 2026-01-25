package com.electricitybilling.controller;

import com.electricitybilling.dto.PayBillRequest;
import com.electricitybilling.dto.PayBillResponse;
import com.electricitybilling.service.PaymentService;
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
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "Payment management endpoints")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(
            summary = "Pay a bill",
            description = "Process payment for a bill. Generates a unique PaymentId, updates bill status to PAID, " +
                    "and returns a confirmation message."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or bill already paid"),
            @ApiResponse(responseCode = "404", description = "Bill not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error or database error")
    })
    @PostMapping("/payBills")
    public ResponseEntity<PayBillResponse> payBill(
            @Valid @RequestBody PayBillRequest request) {
        
        PayBillResponse response = paymentService.payBill(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
