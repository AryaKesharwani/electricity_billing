package com.electricitybilling.users.client;

import com.electricitybilling.users.client.dto.CreateLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AuthServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.auth-service.url:http://auth-service}")
    private String authServiceUrl;

    public void createLogin(String consumerId, String email, String userId, String password) {
        String url = authServiceUrl + "/internal/auth/create-login";
        CreateLoginRequest request = new CreateLoginRequest(consumerId, email, userId, password, "CUSTOMER");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.postForObject(url, new HttpEntity<>(request, headers), Void.class);
    }

    public void deleteLoginByConsumerId(String consumerId) {
        String url = authServiceUrl + "/internal/auth/login-by-consumer/" + java.net.URLEncoder.encode(consumerId, java.nio.charset.StandardCharsets.UTF_8);
        restTemplate.delete(url);
    }
}
