package com.electricitybilling.auth.service;

import com.electricitybilling.auth.dto.CreateLoginRequest;
import com.electricitybilling.auth.entity.Login;
import com.electricitybilling.auth.exception.DuplicateEmailException;
import com.electricitybilling.auth.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthInternalService {

    private final LoginRepository loginRepository;

    @Transactional
    public void createLogin(CreateLoginRequest request) {
        if (loginRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already exists: " + request.getEmail());
        }
        if (request.getUserId() != null && loginRepository.findByUserId(request.getUserId()).isPresent()) {
            throw new DuplicateEmailException("User ID already exists: " + request.getUserId());
        }
        if (request.getConsumerId() != null && loginRepository.findByConsumerId(request.getConsumerId()).isPresent()) {
            throw new DuplicateEmailException("Consumer ID already exists: " + request.getConsumerId());
        }

        Login login = new Login();
        login.setConsumerId(request.getConsumerId());
        login.setEmail(request.getEmail());
        login.setUserId(request.getUserId());
        login.setPassword(request.getPassword());
        login.setUserType(Login.UserType.valueOf(request.getUserType()));
        login.setStatus(Login.AccountStatus.ACTIVE);

        loginRepository.save(login);
    }

    @Transactional
    public void deleteLoginByConsumerId(String consumerId) {
        loginRepository.findByConsumerId(consumerId).ifPresent(loginRepository::delete);
    }

    @Transactional
    public void deleteLoginByEmail(String email) {
        loginRepository.findByEmail(email).ifPresent(loginRepository::delete);
    }
}
