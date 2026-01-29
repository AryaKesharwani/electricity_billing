package com.electricitybilling.controller;

import com.electricitybilling.dto.BillResponse;
import com.electricitybilling.dto.CreateBillRequest;
import com.electricitybilling.dto.UpdateBillStatusRequest;
import com.electricitybilling.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
@Tag(name = "Bills", description = "Bill management endpoints")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @Operation(
            summary = "View bills for a customer",
            description = "Retrieve all bills for a specific customer by consumer ID. Bills are returned in descending order by bill date."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bills retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error or database error")
    })
    @GetMapping("/viewBills")
    public ResponseEntity<List<BillResponse>> viewBills(
            @RequestParam String consumerId) {
        
        List<BillResponse> bills = billService.viewBills(consumerId);
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    @Operation(
            summary = "View all bills",
            description = "Retrieve all bills in the system (for admin use)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bills retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error or database error")
    })
    @GetMapping
    public ResponseEntity<List<BillResponse>> viewAllBills() {
        
        List<BillResponse> bills = billService.viewAllBills();
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    @Operation(
            summary = "Create a new bill",
            description = "Create a new bill for a customer. This is an admin-only operation."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bill created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error or database error")
    })
    @PostMapping
    public ResponseEntity<BillResponse> createBill(
            @Valid @RequestBody CreateBillRequest request) {
        
        BillResponse response = billService.createBill(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update bill status",
            description = "Update a bill's status (PAID, UNPAID, OVERDUE). Admin use."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bill status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "404", description = "Bill not found")
    })
    @PatchMapping("/{billId}/status")
    public ResponseEntity<BillResponse> updateBillStatus(
            @PathVariable Long billId,
            @Valid @RequestBody UpdateBillStatusRequest request) {
        BillResponse response = billService.updateBillStatus(billId, request);
        return ResponseEntity.ok(response);
    }
}
