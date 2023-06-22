package com.tamayo.gcash.exam.service;

import com.tamayo.gcash.exam.dto.DeliveryCostRequestDTO;
import com.tamayo.gcash.exam.dto.DeliveryCostResponseDTO;
import com.tamayo.gcash.exam.dto.VoucherResponseDTO;
import com.tamayo.gcash.exam.service.rules.ParcelRule;
import com.tamayo.gcash.exam.service.voucher.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryCostService {

    private final List<? extends ParcelRule> parcelRules;
    private final VoucherService voucherService;

    public DeliveryCostResponseDTO calculateCost(DeliveryCostRequestDTO requestDto) {
        ParcelRule parcelRule =  parcelRules.stream()
                .filter(rule -> rule.passed(requestDto))
                .findFirst()
                .orElse(null);

        if (parcelRule != null) {
            DeliveryCostResponseDTO responseDTO = parcelRule.getCost(requestDto);
            responseDTO = calcuateWithVoucher(requestDto, responseDTO);
            return responseDTO;
        }

        return DeliveryCostResponseDTO.builder()
                .cost(BigDecimal.ZERO)
                .message("No Result")
                .hasError(true)
                .build();
    }

    public DeliveryCostResponseDTO calcuateWithVoucher(DeliveryCostRequestDTO requestDto, DeliveryCostResponseDTO responseDTO) {
        String message = responseDTO.getMessage();
        BigDecimal currentCost = responseDTO.getCost();
        BigDecimal newCost = currentCost;
        String discount = null;
        boolean hasError = responseDTO.isHasError();

        try {
            if (!hasError && StringUtils.hasText(requestDto.getVoucher())) {
                VoucherResponseDTO voucherResponseDTO = voucherService.getVoucherFromApi(requestDto);
                newCost = voucherService.applyVoucher(voucherResponseDTO, currentCost);
                discount = Float.toString(voucherResponseDTO.getDiscount());
            }
        } catch (Exception ex) {
            discount = String.format("Voucher Issue: %s", ex.getMessage());
        }

        return DeliveryCostResponseDTO.builder()
            .cost(newCost)
            .originalCost(currentCost)
            .discount(discount)
            .message(message)
            .hasError(hasError)
            .build();
    }
}
