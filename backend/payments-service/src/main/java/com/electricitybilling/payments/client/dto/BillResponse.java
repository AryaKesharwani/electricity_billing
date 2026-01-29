package com.electricitybilling.payments.client.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
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
