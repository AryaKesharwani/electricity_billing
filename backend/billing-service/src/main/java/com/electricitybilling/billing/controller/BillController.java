package com.electricitybilling.billing.controller;

import com.electricitybilling.billing.dto.BillResponse;
import com.electricitybilling.billing.dto.CreateBillRequest;
import com.electricitybilling.billing.dto.UpdateBillStatusRequest;
import com.electricitybilling.billing.service.BillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @GetMapping("/viewBills")
    public ResponseEntity<List<BillResponse>> viewBills(@RequestParam String consumerId) {
        List<BillResponse> bills = billService.viewBills(consumerId);
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BillResponse>> viewAllBills() {
        List<BillResponse> bills = billService.viewAllBills();
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BillResponse> createBill(@Valid @RequestBody CreateBillRequest request) {
        BillResponse response = billService.createBill(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{billId}/status")
    public ResponseEntity<BillResponse> updateBillStatus(
            @PathVariable Long billId,
            @Valid @RequestBody UpdateBillStatusRequest request) {
        BillResponse response = billService.updateBillStatus(billId, request);
        return ResponseEntity.ok(response);
    }
}
