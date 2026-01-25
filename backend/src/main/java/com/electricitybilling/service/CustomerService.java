package com.electricitybilling.service;

import com.electricitybilling.dto.RegisterCustomerRequest;
import com.electricitybilling.dto.RegisterCustomerResponse;
import com.electricitybilling.entity.Customer;
import com.electricitybilling.entity.Login;
import com.electricitybilling.exception.DuplicateEmailException;
import com.electricitybilling.exception.InvalidEmailFormatException;
import com.electricitybilling.repository.CustomerRepository;
import com.electricitybilling.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private static final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    private final CustomerRepository customerRepository;
    private final LoginRepository loginRepository;

    @Transactional
    public RegisterCustomerResponse registerCustomer(RegisterCustomerRequest request) {
        try {
            // Validate email format
            validateEmailFormat(request.getEmail());

            // Check for duplicate email in login table
            if (loginRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new DuplicateEmailException("Email already exists: " + request.getEmail());
            }

            // Check if consumer ID already exists
            if (customerRepository.findByConsumerId(request.getConsumerId()).isPresent()) {
                throw new DuplicateEmailException("Consumer ID already exists: " + request.getConsumerId());
            }

            // Check if user ID already exists
            if (loginRepository.findByUserId(request.getUserId()).isPresent()) {
                throw new DuplicateEmailException("User ID already exists: " + request.getUserId());
            }

            // Create Customer entity
            Customer customer = new Customer();
            customer.setConsumerId(request.getConsumerId());
            customer.setCustomerName(request.getCustomerName());
            customer.setEmail(request.getEmail());
            customer.setMobileNumber(request.getMobileNumber());
            customer.setAddress(request.getAddress());

            // Save customer
            customer = customerRepository.save(customer);

            // Create Login entity
            Login login = new Login();
            login.setConsumerId(request.getConsumerId());
            login.setEmail(request.getEmail());
            login.setUserId(request.getUserId());
            login.setPassword(request.getPassword());
            login.setStatus(Login.AccountStatus.ACTIVE);

            // Save login
            loginRepository.save(login);

            // Return response
            return new RegisterCustomerResponse(
                    customer.getConsumerId(),
                    customer.getCustomerName(),
                    customer.getEmail(),
                    "Customer registered successfully"
            );

        } catch (DuplicateEmailException | InvalidEmailFormatException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            // Handle database constraint violations
            if (e.getMessage() != null && e.getMessage().contains("email")) {
                throw new DuplicateEmailException("Email already exists: " + request.getEmail(), e);
            }
            throw new RuntimeException("Database error occurred during registration: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred during customer registration: " + e.getMessage(), e);
        }
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
