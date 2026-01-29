package com.electricitybilling.auth.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UsersServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.users-service.url:http://users-service}")
    private String usersServiceUrl;  // Eureka service id when using @LoadBalanced RestTemplate

    public String getCustomerNameByEmail(String email) {
        try {
            String url = usersServiceUrl + "/internal/customers/customer-name?email=" + java.net.URLEncoder.encode(email, java.nio.charset.StandardCharsets.UTF_8);
            CustomerNameResponse response = restTemplate.getForObject(url, CustomerNameResponse.class);
            return response != null ? response.getCustomerName() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public String getCustomerNameByConsumerId(String consumerId) {
        try {
            String url = usersServiceUrl + "/internal/customers/customer-name?consumerId=" + java.net.URLEncoder.encode(consumerId, java.nio.charset.StandardCharsets.UTF_8);
            CustomerNameResponse response = restTemplate.getForObject(url, CustomerNameResponse.class);
            return response != null ? response.getCustomerName() : null;
        } catch (Exception e) {
            return null;
        }
    }

    @lombok.Data
    public static class CustomerNameResponse {
        private String customerName;
    }
}
