package com.electricitybilling.service;

import com.electricitybilling.dto.LoginRequest;
import com.electricitybilling.dto.LoginResponse;
import com.electricitybilling.entity.Customer;
import com.electricitybilling.entity.Login;
import com.electricitybilling.exception.AccountDeactivatedException;
import com.electricitybilling.exception.InvalidCredentialsException;
import com.electricitybilling.repository.CustomerRepository;
import com.electricitybilling.repository.LoginRepository;
import com.electricitybilling.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private static final String HARDCODED_ADMIN_EMAIL = "admin@electricity.com";
    private static final String HARDCODED_ADMIN_PASSWORD = "admin123";

    private final LoginRepository loginRepository;
    private final CustomerRepository customerRepository;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public LoginResponse validateLogin(LoginRequest request) {
        try {
            // Check hardcoded admin credentials first
            if (HARDCODED_ADMIN_EMAIL.equalsIgnoreCase(request.getUserName())
                    && HARDCODED_ADMIN_PASSWORD.equals(request.getPassword())) {
                String token = jwtUtil.generateToken(
                        HARDCODED_ADMIN_EMAIL,
                        Login.UserType.ADMIN.name(),
                        null
                );
                return new LoginResponse(
                        HARDCODED_ADMIN_EMAIL,
                        Login.UserType.ADMIN.name(),
                        Login.AccountStatus.ACTIVE.name(),
                        "Login successful",
                        token,
                        (String) null
                );
            }

            // Find user by email or userId
            Optional<Login> loginOptional = findUserByUserName(request.getUserName());

            if (loginOptional.isEmpty()) {
                throw new InvalidCredentialsException("Invalid username or password");
            }

            Login login = loginOptional.get();

            // Check if account is deactivated
            if (login.getStatus() == Login.AccountStatus.INACTIVE) {
                throw new AccountDeactivatedException("Account is deactivated. Please contact administrator.");
            }

            // Validate password
            if (!login.getPassword().equals(request.getPassword())) {
                throw new InvalidCredentialsException("Invalid username or password");
            }

            // Generate JWT token
            String token = jwtUtil.generateToken(
                    login.getEmail(),
                    login.getUserType().name(),
                    login.getConsumerId()
            );

            // For CUSTOMER, fetch customer name for welcome message
            String customerName = null;
            if (login.getUserType() == Login.UserType.CUSTOMER) {
                customerName = customerRepository.findByEmail(login.getEmail())
                        .map(Customer::getCustomerName)
                        .orElse(null);
                if (customerName == null && login.getConsumerId() != null) {
                    customerName = customerRepository.findByConsumerId(login.getConsumerId())
                            .map(Customer::getCustomerName)
                            .orElse(null);
                }
            }

            // Return successful login response with JWT token
            return new LoginResponse(
                    login.getEmail(),
                    login.getUserType().name(),
                    login.getStatus().name(),
                    "Login successful",
                    token,
                    customerName
            );

        } catch (InvalidCredentialsException | AccountDeactivatedException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error occurred during login: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred during login: " + e.getMessage(), e);
        }
    }

    private Optional<Login> findUserByUserName(String userName) {
        // Try to find by email first
        Optional<Login> loginByEmail = loginRepository.findByEmail(userName);
        if (loginByEmail.isPresent()) {
            return loginByEmail;
        }

        // If not found by email, try to find by userId
        Optional<Login> loginByUserId = loginRepository.findByUserId(userName);
        if (loginByUserId.isPresent()) {
            return loginByUserId;
        }

        // If not found by userId, try to find by consumerId (for customers)
        Optional<Login> loginByConsumerId = loginRepository.findByConsumerId(userName);
        return loginByConsumerId;
    }
}
