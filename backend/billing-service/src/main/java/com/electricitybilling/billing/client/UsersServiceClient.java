package com.electricitybilling.billing.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UsersServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.users-service.url:http://users-service}")
    private String usersServiceUrl;

    public boolean customerExists(String consumerId) {
        try {
            String url = usersServiceUrl + "/internal/customers/consumer/" + java.net.URLEncoder.encode(consumerId, java.nio.charset.StandardCharsets.UTF_8);
            restTemplate.getForEntity(url, Void.class);
            return true;
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            return false;
        }
    }
}
