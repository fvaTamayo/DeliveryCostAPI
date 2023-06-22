package com.tamayo.gcash.exam.service.voucher;

import com.tamayo.gcash.exam.configuration.VoucherRestConfig;
import com.tamayo.gcash.exam.dto.DeliveryCostRequestDTO;
import com.tamayo.gcash.exam.dto.VoucherResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:messages.properties")
public class VoucherService {

    @Autowired
    private VoucherRestInterface voucherRestInterface;

    @Autowired
    private VoucherRestConfig voucherRestConfig;

    @Value("${voucher.expired.message}")
    private String expiredVoucherMessage;

    @Value("${value.precision}")
    private int precision;

    public VoucherResponseDTO getVoucherFromApi(DeliveryCostRequestDTO requestDto) throws Exception {
        return voucherRestInterface.getVoucher(requestDto.getVoucher());
    }

    public BigDecimal applyVoucher(VoucherResponseDTO voucherResponseDTO, BigDecimal cost) throws Exception {
        if (!voucherRestConfig.isExpiredAllowed() && voucherResponseDTO.getExpiry().isBefore(LocalDate.now())) {
            throw new Exception(expiredVoucherMessage);
        }
        BigDecimal discount = BigDecimal.valueOf(voucherResponseDTO.getDiscount());
        BigDecimal newCost = discount.compareTo(cost) == 1 ? BigDecimal.ZERO : (cost.subtract(discount)).setScale(precision);
        return newCost;
    }
}
