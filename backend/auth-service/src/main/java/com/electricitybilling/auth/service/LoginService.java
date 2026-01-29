package com.electricitybilling.auth.service;

import com.electricitybilling.auth.client.UsersServiceClient;
import com.electricitybilling.auth.dto.LoginRequest;
import com.electricitybilling.auth.dto.LoginResponse;
import com.electricitybilling.auth.entity.Login;
import com.electricitybilling.auth.exception.AccountDeactivatedException;
import com.electricitybilling.auth.exception.InvalidCredentialsException;
import com.electricitybilling.auth.repository.LoginRepository;
import com.electricitybilling.auth.util.JwtUtil;
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
    private final JwtUtil jwtUtil;
    private final UsersServiceClient usersServiceClient;

    @Transactional(readOnly = true)
    public LoginResponse validateLogin(LoginRequest request) {
        try {
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

            Optional<Login> loginOptional = findUserByUserName(request.getUserName());

            if (loginOptional.isEmpty()) {
                throw new InvalidCredentialsException("Invalid username or password");
            }

            Login login = loginOptional.get();

            if (login.getStatus() == Login.AccountStatus.INACTIVE) {
                throw new AccountDeactivatedException("Account is deactivated. Please contact administrator.");
            }

            if (!login.getPassword().equals(request.getPassword())) {
                throw new InvalidCredentialsException("Invalid username or password");
            }

            String token = jwtUtil.generateToken(
                    login.getEmail(),
                    login.getUserType().name(),
                    login.getConsumerId()
            );

            String customerName = null;
            if (login.getUserType() == Login.UserType.CUSTOMER) {
                customerName = usersServiceClient.getCustomerNameByEmail(login.getEmail());
                if (customerName == null && login.getConsumerId() != null) {
                    customerName = usersServiceClient.getCustomerNameByConsumerId(login.getConsumerId());
                }
            }

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
        Optional<Login> loginByEmail = loginRepository.findByEmail(userName);
        if (loginByEmail.isPresent()) {
            return loginByEmail;
        }
        Optional<Login> loginByUserId = loginRepository.findByUserId(userName);
        if (loginByUserId.isPresent()) {
            return loginByUserId;
        }
        return loginRepository.findByConsumerId(userName);
    }
}
