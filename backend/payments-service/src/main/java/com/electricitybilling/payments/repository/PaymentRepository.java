package com.electricitybilling.payments.repository;

import com.electricitybilling.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentUuid(String paymentUuid);

    List<Payment> findByConsumerId(String consumerId);
}
