package com.tamayo.gcash.exam.service.rules;

import com.tamayo.gcash.exam.dto.DeliveryCostRequestDTO;
import com.tamayo.gcash.exam.dto.DeliveryCostResponseDTO;
import com.tamayo.gcash.exam.configuration.ParcelConfig;
import com.tamayo.gcash.exam.service.rules.utils.ParcelType;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order(5)
public class LargeParcelRule extends AbstractParcelRule {

    public LargeParcelRule(ParcelConfig parcelConfig) {
        super(parcelConfig);
        this.parcelType = ParcelType.large.name();
    }

    @Override
    public boolean passed(DeliveryCostRequestDTO requestDto) {
        BigDecimal volume = getVolume(requestDto), range = getRange();
        return volume.compareTo(range) == 1 || volume.compareTo(range) == 0;
    }

    @Override
    public DeliveryCostResponseDTO getCost(DeliveryCostRequestDTO requestDto) {
        return getCostBasedOnSize(requestDto);
    }
}
