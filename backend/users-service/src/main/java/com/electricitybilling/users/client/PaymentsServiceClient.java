package com.electricitybilling.users.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class PaymentsServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.payments-service.url:http://payments-service}")
    private String paymentsServiceUrl;

    public void deletePaymentsByConsumerId(String consumerId) {
        String url = paymentsServiceUrl + "/internal/payments/consumer/" + java.net.URLEncoder.encode(consumerId, java.nio.charset.StandardCharsets.UTF_8);
        restTemplate.delete(url);
    }
}
