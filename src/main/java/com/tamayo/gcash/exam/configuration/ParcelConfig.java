package com.tamayo.gcash.exam.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "rule")
@Data
public class ParcelConfig {
    private Map<String, BigDecimal> max;
    private Map<String, BigDecimal> range;
    private Map<String, BigDecimal> multiplier;
}