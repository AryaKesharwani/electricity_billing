package com.electricitybilling.payments.client;

import com.electricitybilling.payments.client.dto.BillResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class BillingServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.billing-service.url:http://billing-service}")
    private String billingServiceUrl;

    public BillResponse getBill(Long billId) {
        String url = billingServiceUrl + "/internal/bills/" + billId;
        return restTemplate.getForObject(url, BillResponse.class);
    }

    public void updateBillStatus(Long billId, String status) {
        String url = billingServiceUrl + "/internal/bills/" + billId + "/status";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UpdateStatusRequest request = new UpdateStatusRequest(status);
        restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                new HttpEntity<>(request, headers),
                BillResponse.class
        );
    }

    @lombok.Data
    private static class UpdateStatusRequest {
        private final String status;

        UpdateStatusRequest(String status) {
            this.status = status;
        }
    }
}
