package com.tamayo.gcash.exam.service.rules;

import com.tamayo.gcash.exam.dto.DeliveryCostRequestDTO;
import com.tamayo.gcash.exam.dto.DeliveryCostResponseDTO;
import com.tamayo.gcash.exam.configuration.ParcelConfig;
import com.tamayo.gcash.exam.service.rules.utils.ParcelType;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class HeavyParcelRule extends AbstractParcelRule {

    public HeavyParcelRule(ParcelConfig parcelConfig) {
        super(parcelConfig);
        this.parcelType = ParcelType.heavy.name();
    }

    @Override
    public boolean passed(DeliveryCostRequestDTO requestDto) {
        return requestDto.getWeight().compareTo(getRange()) == 1;
    }

    @Override
    public DeliveryCostResponseDTO getCost(DeliveryCostRequestDTO requestDto) {
        return DeliveryCostResponseDTO.builder()
            .cost((getMultiplier().multiply(requestDto.getWeight())).setScale(precision))
            .message(String.format("%s %s", parcelTypeMessage, parcelType))
            .build();
    }
}
