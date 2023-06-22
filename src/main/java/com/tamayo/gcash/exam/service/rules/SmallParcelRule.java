package com.tamayo.gcash.exam.service.rules;

import com.tamayo.gcash.exam.dto.DeliveryCostRequestDTO;
import com.tamayo.gcash.exam.dto.DeliveryCostResponseDTO;
import com.tamayo.gcash.exam.configuration.ParcelConfig;
import com.tamayo.gcash.exam.service.rules.utils.ParcelType;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class SmallParcelRule extends AbstractParcelRule {

    public SmallParcelRule(ParcelConfig parcelConfig) {
        super(parcelConfig);
        this.parcelType = ParcelType.small.name();
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
