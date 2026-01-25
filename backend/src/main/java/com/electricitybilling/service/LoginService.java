package com.electricitybilling.service;

import com.electricitybilling.dto.LoginRequest;
import com.electricitybilling.dto.LoginResponse;
import com.electricitybilling.entity.Login;
import com.electricitybilling.exception.AccountDeactivatedException;
import com.electricitybilling.exception.InvalidCredentialsException;
import com.electricitybilling.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginRepository loginRepository;

    @Transactional(readOnly = true)
    public LoginResponse validateLogin(LoginRequest request) {
        try {
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

            // Return successful login response
            return new LoginResponse(
                    login.getEmail(),
                    login.getUserType().name(),
                    login.getStatus().name(),
                    "Login successful"
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
