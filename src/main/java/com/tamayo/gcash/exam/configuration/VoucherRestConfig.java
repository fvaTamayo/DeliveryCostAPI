package com.tamayo.gcash.exam.configuration;

import com.tamayo.gcash.exam.service.voucher.VoucherRestErrorDecoder;
import feign.codec.ErrorDecoder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "voucher")
@Data
public class VoucherRestConfig {

    private String url;
    private String action;
    private String key;
    private boolean expiredAllowed;

    @Bean
    public ErrorDecoder errorDecoder() {
        return new VoucherRestErrorDecoder();
    }
}
