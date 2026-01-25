package com.electricitybilling.repository;

import com.electricitybilling.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    Optional<Payment> findByPaymentUuid(String paymentUuid);
    
    List<Payment> findByBillId(Long billId);
    
    List<Payment> findByConsumerId(String consumerId);
}
