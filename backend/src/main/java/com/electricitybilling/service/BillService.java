package com.electricitybilling.service;

import com.electricitybilling.dto.BillResponse;
import com.electricitybilling.dto.CreateBillRequest;
import com.electricitybilling.entity.Bill;
import com.electricitybilling.exception.CustomerNotFoundException;
import com.electricitybilling.repository.BillRepository;
import com.electricitybilling.repository.CustomerRepository;
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
    private final CustomerRepository customerRepository;

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

    @Transactional
    public BillResponse createBill(CreateBillRequest request) {
        try {
            // Validate that customer exists
            if (!customerRepository.findByConsumerId(request.getConsumerId()).isPresent()) {
                throw new CustomerNotFoundException("Customer not found with Consumer ID: " + request.getConsumerId());
            }

            // Validate dates
            if (request.getDueDate().isBefore(request.getBillDate())) {
                throw new IllegalArgumentException("Due date cannot be before bill date");
            }

            // Create bill entity
            Bill bill = new Bill();
            bill.setConsumerId(request.getConsumerId());
            bill.setBillDate(request.getBillDate());
            bill.setDueDate(request.getDueDate());
            bill.setUnitsConsumed(request.getUnitsConsumed());
            bill.setAmount(request.getAmount());
            bill.setDescription(request.getDescription());
            bill.setStatus(Bill.BillStatus.UNPAID);

            // Save bill
            bill = billRepository.save(bill);

            // Return response
            return convertToResponse(bill);

        } catch (CustomerNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error occurred while creating bill: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while creating bill: " + e.getMessage(), e);
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
