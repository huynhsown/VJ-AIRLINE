package com.vietjoke.vn.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class PaypalConfig {

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.secret}")
    private String secretKey;

    @Value("${paypal.api-url}")
    private String apiUrl;

}
