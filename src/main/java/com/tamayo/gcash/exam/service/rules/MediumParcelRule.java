package com.tamayo.gcash.exam.service.rules;

import com.tamayo.gcash.exam.dto.DeliveryCostRequestDTO;
import com.tamayo.gcash.exam.dto.DeliveryCostResponseDTO;
import com.tamayo.gcash.exam.configuration.ParcelConfig;
import com.tamayo.gcash.exam.service.rules.utils.ParcelType;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
public class MediumParcelRule extends AbstractParcelRule {

    public MediumParcelRule(ParcelConfig parcelConfig) {
        super(parcelConfig);
        this.parcelType = ParcelType.medium.name();
    }

    @Override
    public boolean passed(DeliveryCostRequestDTO requestDto) {
        return getVolume(requestDto).compareTo(getRange()) == -1;
    }

    @Override
    public DeliveryCostResponseDTO getCost(DeliveryCostRequestDTO requestDto) {
        return getCostBasedOnSize(requestDto);
    }
}
