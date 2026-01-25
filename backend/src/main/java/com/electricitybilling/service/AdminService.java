package com.electricitybilling.service;

import com.electricitybilling.dto.RegisterAdminRequest;
import com.electricitybilling.dto.RegisterAdminResponse;
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
public class AdminService {

    private static final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    private final LoginRepository loginRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public RegisterAdminResponse registerAdmin(RegisterAdminRequest request) {
        try {
            // Validate email format
            validateEmailFormat(request.getEmail());

            // Check if email already exists in login table
            if (loginRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new DuplicateEmailException("Email already exists: " + request.getEmail());
            }

            // Check if email exists in customer table (admin email should not match any customer's email)
            if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new DuplicateEmailException("Email already exists as a customer: " + request.getEmail());
            }

            // Create Login entity for admin
            Login adminLogin = new Login();
            adminLogin.setEmail(request.getEmail());
            adminLogin.setPassword(request.getPassword());
            adminLogin.setUserType(Login.UserType.ADMIN);
            adminLogin.setStatus(Login.AccountStatus.ACTIVE);
            // consumer_id and user_id are null for admin accounts

            // Save admin login
            adminLogin = loginRepository.save(adminLogin);

            // Return response
            return new RegisterAdminResponse(
                    adminLogin.getEmail(),
                    adminLogin.getUserType().name(),
                    adminLogin.getStatus().name(),
                    "Administrator registered successfully"
            );

        } catch (DuplicateEmailException | InvalidEmailFormatException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            // Handle database constraint violations
            if (e.getMessage() != null && e.getMessage().contains("email")) {
                throw new DuplicateEmailException("Email already exists: " + request.getEmail(), e);
            }
            throw new RuntimeException("Database error occurred during admin registration: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred during admin registration: " + e.getMessage(), e);
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
