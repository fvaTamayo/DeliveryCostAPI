package com.tamayo.gcash.exam.service.rules;

import com.tamayo.gcash.exam.dto.DeliveryCostRequestDTO;
import com.tamayo.gcash.exam.dto.DeliveryCostResponseDTO;

public interface ParcelRule {
    DeliveryCostResponseDTO getCost(DeliveryCostRequestDTO requestDto);

    boolean passed(DeliveryCostRequestDTO requestDto);
}
