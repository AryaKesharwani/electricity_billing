package com.electricitybilling.payments.service;

import com.electricitybilling.payments.client.BillingServiceClient;
import com.electricitybilling.payments.client.dto.BillResponse;
import com.electricitybilling.payments.dto.PayBillRequest;
import com.electricitybilling.payments.dto.PayBillResponse;
import com.electricitybilling.payments.entity.Payment;
import com.electricitybilling.payments.exception.BillAlreadyPaidException;
import com.electricitybilling.payments.exception.BillNotFoundException;
import com.electricitybilling.payments.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillingServiceClient billingServiceClient;

    @Transactional
    public PayBillResponse payBill(PayBillRequest request) {
        BillResponse billResponse;
        try {
            billResponse = billingServiceClient.getBill(request.getBillId());
        } catch (HttpClientErrorException.NotFound e) {
            throw new BillNotFoundException("Bill not found with ID: " + request.getBillId());
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to fetch bill: " + e.getMessage(), e);
        }

        if (billResponse == null) {
            throw new BillNotFoundException("Bill not found with ID: " + request.getBillId());
        }

        if ("PAID".equals(billResponse.getStatus())) {
            throw new BillAlreadyPaidException("Bill with ID " + request.getBillId() + " is already paid");
        }

        if (request.getAmount().compareTo(billResponse.getAmount()) > 0) {
            throw new IllegalArgumentException("Payment amount cannot exceed bill amount");
        }

        String paymentUuid = generateUniquePaymentId();

        Payment payment = new Payment();
        payment.setPaymentUuid(paymentUuid);
        payment.setBillId(billResponse.getBillId());
        payment.setConsumerId(billResponse.getConsumerId());
        payment.setAmountPaid(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "ONLINE");
        payment.setTransactionReference(request.getTransactionReference());
        payment.setStatus(Payment.PaymentStatus.SUCCESS);

        payment = paymentRepository.save(payment);

        billingServiceClient.updateBillStatus(request.getBillId(), "PAID");

        return new PayBillResponse(
                payment.getPaymentUuid(),
                billResponse.getBillId(),
                billResponse.getConsumerId(),
                payment.getAmountPaid(),
                payment.getStatus().name(),
                "Payment successful. Your bill has been paid successfully.",
                payment.getPaymentDate(),
                payment.getTransactionReference()
        );
    }

    @Transactional
    public void deletePaymentsByConsumerId(String consumerId) {
        paymentRepository.deleteAll(paymentRepository.findByConsumerId(consumerId));
    }

    private String generateUniquePaymentId() {
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
