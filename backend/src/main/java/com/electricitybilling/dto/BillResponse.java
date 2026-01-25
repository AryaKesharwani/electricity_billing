package com.electricitybilling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillResponse {

    private Long billId;
    private String consumerId;
    private LocalDate billDate;
    private LocalDate dueDate;
    private Double unitsConsumed;
    private BigDecimal amount;
    private String status;
    private String description;
    private LocalDateTime createdAt;
}
