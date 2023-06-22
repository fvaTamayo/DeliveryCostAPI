package com.tamayo.gcash.exam.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DeliveryCostResponseDTO {
    private BigDecimal cost;
    @Builder.Default
    private String message = "";
    private boolean hasError;

    private BigDecimal originalCost;
    private String discount;
}
