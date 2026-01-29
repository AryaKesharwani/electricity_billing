package com.electricitybilling.billing.service;

import com.electricitybilling.billing.client.UsersServiceClient;
import com.electricitybilling.billing.dto.BillResponse;
import com.electricitybilling.billing.dto.CreateBillRequest;
import com.electricitybilling.billing.dto.UpdateBillStatusRequest;
import com.electricitybilling.billing.entity.Bill;
import com.electricitybilling.billing.exception.BillNotFoundException;
import com.electricitybilling.billing.exception.CustomerNotFoundException;
import com.electricitybilling.billing.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final UsersServiceClient usersServiceClient;

    @Transactional(readOnly = true)
    public List<BillResponse> viewBills(String consumerId) {
        try {
            List<Bill> bills = billRepository.findByConsumerIdOrderByBillDateDesc(consumerId);
            return bills.stream().map(this::convertToResponse).collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error occurred while fetching bills: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<BillResponse> viewAllBills() {
        try {
            List<Bill> bills = billRepository.findAll();
            return bills.stream().map(this::convertToResponse).collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error occurred while fetching bills: " + e.getMessage(), e);
        }
    }

    @Transactional
    public BillResponse createBill(CreateBillRequest request) {
        if (!usersServiceClient.customerExists(request.getConsumerId())) {
            throw new CustomerNotFoundException("Customer not found with Consumer ID: " + request.getConsumerId());
        }
        if (request.getDueDate().isBefore(request.getBillDate())) {
            throw new IllegalArgumentException("Due date cannot be before bill date");
        }

        Bill bill = new Bill();
        bill.setConsumerId(request.getConsumerId());
        bill.setBillDate(request.getBillDate());
        bill.setDueDate(request.getDueDate());
        bill.setUnitsConsumed(request.getUnitsConsumed());
        bill.setAmount(request.getAmount());
        bill.setDescription(request.getDescription());
        bill.setStatus(Bill.BillStatus.UNPAID);

        bill = billRepository.save(bill);
        return convertToResponse(bill);
    }

    @Transactional
    public BillResponse updateBillStatus(Long billId, UpdateBillStatusRequest request) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new BillNotFoundException("Bill not found with ID: " + billId));
        bill.setStatus(Bill.BillStatus.valueOf(request.getStatus()));
        bill = billRepository.save(bill);
        return convertToResponse(bill);
    }

    @Transactional(readOnly = true)
    public BillResponse getBill(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new BillNotFoundException("Bill not found with ID: " + billId));
        return convertToResponse(bill);
    }

    @Transactional
    public void deleteBillsByConsumerId(String consumerId) {
        List<Bill> bills = billRepository.findByConsumerId(consumerId);
        billRepository.deleteAll(bills);
    }

    private BillResponse convertToResponse(Bill bill) {
        BillResponse response = new BillResponse();
        response.setBillId(bill.getBillId());
        response.setConsumerId(bill.getConsumerId());
        response.setBillDate(bill.getBillDate());
        response.setDueDate(bill.getDueDate());
        response.setUnitsConsumed(bill.getUnitsConsumed());
        response.setAmount(bill.getAmount());
        response.setStatus(bill.getStatus().name());
        response.setDescription(bill.getDescription());
        response.setCreatedAt(bill.getCreatedAt());
        return response;
    }
}
