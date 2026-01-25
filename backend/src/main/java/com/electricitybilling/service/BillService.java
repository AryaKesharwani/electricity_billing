package com.electricitybilling.service;

import com.electricitybilling.dto.BillResponse;
import com.electricitybilling.entity.Bill;
import com.electricitybilling.repository.BillRepository;
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

    @Transactional(readOnly = true)
    public List<BillResponse> viewBills(String consumerId) {
        try {
            List<Bill> bills = billRepository.findByConsumerIdOrderByBillDateDesc(consumerId);
            
            return bills.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
                    
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error occurred while fetching bills: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while fetching bills: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<BillResponse> viewAllBills() {
        try {
            List<Bill> bills = billRepository.findAll();
            
            return bills.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
                    
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error occurred while fetching bills: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while fetching bills: " + e.getMessage(), e);
        }
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
