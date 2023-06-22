package com.tamayo.gcash.exam.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class
DeliveryCostRequestDTO {

    @NotNull(message = "{field.required.message}")
    private BigDecimal weight;

    @NotNull(message = "{field.required.message}")
    private BigDecimal height;

    @NotNull(message = "{field.required.message}")
    private BigDecimal width;

    @NotNull(message = "{field.required.message}")
    private BigDecimal length;

    private String voucher;
}
