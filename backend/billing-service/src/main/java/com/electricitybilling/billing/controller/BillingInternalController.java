package com.electricitybilling.billing.controller;

import com.electricitybilling.billing.dto.BillResponse;
import com.electricitybilling.billing.dto.UpdateBillStatusRequest;
import com.electricitybilling.billing.service.BillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/bills")
@RequiredArgsConstructor
public class BillingInternalController {

    private final BillService billService;

    @GetMapping("/{billId}")
    public ResponseEntity<BillResponse> getBill(@PathVariable Long billId) {
        BillResponse bill = billService.getBill(billId);
        return ResponseEntity.ok(bill);
    }

    @PatchMapping("/{billId}/status")
    public ResponseEntity<BillResponse> updateBillStatus(
            @PathVariable Long billId,
            @Valid @RequestBody UpdateBillStatusRequest request) {
        BillResponse response = billService.updateBillStatus(billId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/consumer/{consumerId}")
    public ResponseEntity<Void> deleteBillsByConsumerId(@PathVariable String consumerId) {
        billService.deleteBillsByConsumerId(consumerId);
        return ResponseEntity.noContent().build();
    }
}
