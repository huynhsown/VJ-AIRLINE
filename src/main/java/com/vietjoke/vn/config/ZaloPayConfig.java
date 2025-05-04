package com.vietjoke.vn.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ZaloPayConfig {
    @Value("${zalo_pay.app_id}")
    private String appId;

    @Value("${zalo_pay.mac_key}")
    private String macKey;

    @Value("${zalo_pay.url_create_order}")
    private String urlCreateOrder;
}
