package com.electricitybilling.service;

import com.electricitybilling.dto.PayBillRequest;
import com.electricitybilling.dto.PayBillResponse;
import com.electricitybilling.entity.Bill;
import com.electricitybilling.entity.Payment;
import com.electricitybilling.exception.BillNotFoundException;
import com.electricitybilling.exception.BillAlreadyPaidException;
import com.electricitybilling.repository.BillRepository;
import com.electricitybilling.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public PayBillResponse payBill(PayBillRequest request) {
        try {
            // Find the bill
            Bill bill = billRepository.findById(request.getBillId())
                    .orElseThrow(() -> new BillNotFoundException("Bill not found with ID: " + request.getBillId()));

            // Check if bill is already paid
            if (bill.getStatus() == Bill.BillStatus.PAID) {
                throw new BillAlreadyPaidException("Bill with ID " + request.getBillId() + " is already paid");
            }

            // Validate payment amount (should match or be less than bill amount)
            if (request.getAmount().compareTo(bill.getAmount()) > 0) {
                throw new IllegalArgumentException("Payment amount cannot exceed bill amount");
            }

            // Generate unique PaymentId (UUID)
            String paymentUuid = generateUniquePaymentId();

            // Create payment record
            Payment payment = new Payment();
            payment.setPaymentUuid(paymentUuid);
            payment.setBillId(bill.getBillId());
            payment.setConsumerId(bill.getConsumerId());
            payment.setAmountPaid(request.getAmount());
            payment.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "ONLINE");
            payment.setTransactionReference(request.getTransactionReference());
            payment.setStatus(Payment.PaymentStatus.SUCCESS);

            // Save payment
            payment = paymentRepository.save(payment);

            // Update bill status to PAID
            bill.setStatus(Bill.BillStatus.PAID);
            billRepository.save(bill);

            // Return success response
            return new PayBillResponse(
                    payment.getPaymentUuid(),
                    bill.getBillId(),
                    bill.getConsumerId(),
                    payment.getAmountPaid(),
                    payment.getStatus().name(),
                    "Payment successful. Your bill has been paid successfully.",
                    payment.getPaymentDate(),
                    payment.getTransactionReference()
            );

        } catch (BillNotFoundException | BillAlreadyPaidException | IllegalArgumentException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error occurred during payment processing: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred during payment processing: " + e.getMessage(), e);
        }
    }

    private String generateUniquePaymentId() {
        // Generate UUID and ensure uniqueness
        String paymentUuid;
        int attempts = 0;
        do {
            paymentUuid = "PAY-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
            attempts++;
            if (attempts > 10) {
                throw new RuntimeException("Unable to generate unique payment ID after multiple attempts");
            }
        } while (paymentRepository.findByPaymentUuid(paymentUuid).isPresent());
        
        return paymentUuid;
    }
}
