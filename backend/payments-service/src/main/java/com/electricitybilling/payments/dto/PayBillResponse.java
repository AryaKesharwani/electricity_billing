package com.electricitybilling.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayBillResponse {

    private String paymentId;
    private Long billId;
    private String consumerId;
    private BigDecimal amountPaid;
    private String paymentStatus;
    private String message;
    private LocalDateTime paymentDate;
    private String transactionReference;
}
