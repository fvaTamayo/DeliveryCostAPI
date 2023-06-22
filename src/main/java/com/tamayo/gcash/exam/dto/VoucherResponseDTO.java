package com.tamayo.gcash.exam.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class VoucherResponseDTO {
    private String code;
    private float discount;
    private LocalDate expiry;
}
