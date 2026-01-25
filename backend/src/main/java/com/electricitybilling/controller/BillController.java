package com.electricitybilling.controller;

import com.electricitybilling.dto.BillResponse;
import com.electricitybilling.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
}
