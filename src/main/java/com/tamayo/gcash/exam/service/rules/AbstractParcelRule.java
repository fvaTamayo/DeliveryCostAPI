package com.tamayo.gcash.exam.service.rules;

import com.tamayo.gcash.exam.dto.DeliveryCostRequestDTO;
import com.tamayo.gcash.exam.dto.DeliveryCostResponseDTO;
import com.tamayo.gcash.exam.configuration.ParcelConfig;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;
import java.util.Objects;

@PropertySource("classpath:messages.properties")
@RequiredArgsConstructor
public abstract class AbstractParcelRule implements ParcelRule {

    Logger LOGGER = LoggerFactory.getLogger(AbstractParcelRule.class);

    private final ParcelConfig parcelConfig;

    @Value("${value.precision}")
    protected int precision;
    protected final MathContext mc = new MathContext(precision);
    @Value("${parcel.type.message}")
    protected String parcelTypeMessage;
    protected String parcelType;
    protected String errorMessage;

    protected Map<String, BigDecimal> getMax() {
        Map<String, BigDecimal> maxMap = parcelConfig.getMax();
        maxMap.values().removeIf(Objects::isNull);
        return maxMap;
    }

    protected BigDecimal getRange() {
        return parcelConfig.getRange().get(parcelType);
    }

    protected BigDecimal getMultiplier() {
        return parcelConfig.getMultiplier().get(parcelType);
    }

    public BigDecimal getVolume(DeliveryCostRequestDTO requestDto) {
        return (requestDto.getWidth().
            multiply(requestDto.getHeight()).
            multiply(requestDto.getLength())).setScale(precision);
    }

    public DeliveryCostResponseDTO getCostBasedOnSize(DeliveryCostRequestDTO requestDto) {
        return DeliveryCostResponseDTO.builder()
                .cost(getMultiplier().multiply(getVolume(requestDto)).setScale(precision))
                .message(String.format("%s %s", parcelTypeMessage, parcelType))
                .build();
    }
}
