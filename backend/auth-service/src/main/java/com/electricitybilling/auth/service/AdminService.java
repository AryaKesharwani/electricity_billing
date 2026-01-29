package com.electricitybilling.auth.service;

import com.electricitybilling.auth.dto.RegisterAdminRequest;
import com.electricitybilling.auth.dto.RegisterAdminResponse;
import com.electricitybilling.auth.entity.Login;
import com.electricitybilling.auth.exception.DuplicateEmailException;
import com.electricitybilling.auth.exception.InvalidEmailFormatException;
import com.electricitybilling.auth.repository.LoginRepository;
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

    @Transactional
    public RegisterAdminResponse registerAdmin(RegisterAdminRequest request) {
        try {
            validateEmailFormat(request.getEmail());

            if (loginRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new DuplicateEmailException("Email already exists: " + request.getEmail());
            }

            Login adminLogin = new Login();
            adminLogin.setEmail(request.getEmail());
            adminLogin.setPassword(request.getPassword());
            adminLogin.setUserType(Login.UserType.ADMIN);
            adminLogin.setStatus(Login.AccountStatus.ACTIVE);

            adminLogin = loginRepository.save(adminLogin);

            return new RegisterAdminResponse(
                    adminLogin.getEmail(),
                    adminLogin.getUserType().name(),
                    adminLogin.getStatus().name(),
                    "Administrator registered successfully"
            );

        } catch (DuplicateEmailException | InvalidEmailFormatException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
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
