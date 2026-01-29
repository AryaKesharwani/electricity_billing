package com.electricitybilling.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBillStatusRequest {

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "PAID|UNPAID|OVERDUE", message = "Status must be PAID, UNPAID, or OVERDUE")
    private String status;
}
