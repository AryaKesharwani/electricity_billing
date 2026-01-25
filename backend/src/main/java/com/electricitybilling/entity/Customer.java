package com.electricitybilling.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @Column(name = "consumer_id", unique = true, nullable = false)
    @NotBlank(message = "Consumer ID is required")
    private String consumerId;

    @Column(name = "customer_name", nullable = false)
    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;

    @Column(name = "mobile_number", nullable = false)
    @NotBlank(message = "Mobile number is required")
    private String mobileNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
