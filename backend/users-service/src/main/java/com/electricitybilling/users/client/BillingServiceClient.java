package com.electricitybilling.users.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class BillingServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.billing-service.url:http://billing-service}")
    private String billingServiceUrl;

    public void deleteBillsByConsumerId(String consumerId) {
        String url = billingServiceUrl + "/internal/bills/consumer/" + java.net.URLEncoder.encode(consumerId, java.nio.charset.StandardCharsets.UTF_8);
        restTemplate.delete(url);
    }
}
