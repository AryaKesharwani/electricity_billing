package com.electricitybilling.users.service;

import com.electricitybilling.users.client.AuthServiceClient;
import com.electricitybilling.users.client.BillingServiceClient;
import com.electricitybilling.users.client.PaymentsServiceClient;
import com.electricitybilling.users.dto.CustomerListItem;
import com.electricitybilling.users.dto.RegisterCustomerRequest;
import com.electricitybilling.users.dto.RegisterCustomerResponse;
import com.electricitybilling.users.entity.Customer;
import com.electricitybilling.users.exception.CustomerNotFoundException;
import com.electricitybilling.users.exception.DuplicateEmailException;
import com.electricitybilling.users.exception.InvalidEmailFormatException;
import com.electricitybilling.users.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    private final CustomerRepository customerRepository;
    private final AuthServiceClient authServiceClient;
    private final BillingServiceClient billingServiceClient;
    private final PaymentsServiceClient paymentsServiceClient;

    @Transactional
    public RegisterCustomerResponse registerCustomer(RegisterCustomerRequest request) {
        validateEmailFormat(request.getEmail());

        if (customerRepository.findByConsumerId(request.getConsumerId()).isPresent()) {
            throw new DuplicateEmailException("Consumer ID already exists: " + request.getConsumerId());
        }
        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already exists: " + request.getEmail());
        }

        Customer customer = new Customer();
        customer.setConsumerId(request.getConsumerId());
        customer.setCustomerName(request.getCustomerName());
        customer.setEmail(request.getEmail());
        customer.setMobileNumber(request.getMobileNumber());
        customer.setAddress(request.getAddress());

        customer = customerRepository.save(customer);

        try {
            authServiceClient.createLogin(
                    request.getConsumerId(),
                    request.getEmail(),
                    request.getUserId(),
                    request.getPassword()
            );
        } catch (RestClientException e) {
            customerRepository.delete(customer);
            if (e instanceof HttpClientErrorException ex && ex.getStatusCode() == HttpStatus.CONFLICT) {
                throw new DuplicateEmailException("Email or User ID already exists in system", e);
            }
            throw new RuntimeException("Failed to create login credentials: " + e.getMessage(), e);
        }

        return new RegisterCustomerResponse(
                customer.getConsumerId(),
                customer.getCustomerName(),
                customer.getEmail(),
                "Customer registered successfully"
        );
    }

    @Transactional(readOnly = true)
    public List<CustomerListItem> findAllCustomers() {
        return customerRepository.findAll().stream()
                .map(c -> new CustomerListItem(
                        c.getConsumerId(),
                        c.getCustomerName(),
                        c.getEmail(),
                        c.getMobileNumber(),
                        c.getAddress() != null ? c.getAddress() : ""
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCustomer(String consumerId) {
        Customer customer = customerRepository.findByConsumerId(consumerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with Consumer ID: " + consumerId));

        try {
            paymentsServiceClient.deletePaymentsByConsumerId(consumerId);
        } catch (Exception e) {
            // log and continue
        }
        try {
            billingServiceClient.deleteBillsByConsumerId(consumerId);
        } catch (Exception e) {
            // log and continue
        }
        try {
            authServiceClient.deleteLoginByConsumerId(consumerId);
        } catch (Exception e) {
            // log and continue
        }

        customerRepository.delete(customer);
    }

    public String getCustomerNameByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(Customer::getCustomerName)
                .orElse(null);
    }

    public String getCustomerNameByConsumerId(String consumerId) {
        return customerRepository.findByConsumerId(consumerId)
                .map(Customer::getCustomerName)
                .orElse(null);
    }

    public boolean existsByConsumerId(String consumerId) {
        return customerRepository.findByConsumerId(consumerId).isPresent();
    }

    private void validateEmailFormat(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidEmailFormatException("Email cannot be null or empty");
        }
        if (!pattern.matcher(email).matches()) {
            throw new InvalidEmailFormatException("Invalid email format: " + email);
        }
    }
}
