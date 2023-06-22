package com.tamayo.gcash.exam.service.voucher;

import com.tamayo.gcash.exam.configuration.VoucherRestConfig;
import com.tamayo.gcash.exam.dto.VoucherResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "${voucher.action}", url = "${voucher.url}", configuration = VoucherRestConfig.class)
public interface VoucherRestInterface {

    @GetMapping(value = "/${voucher.action}/{code}?key=${voucher.key}")
    public VoucherResponseDTO getVoucher(@PathVariable("code") String code) throws Exception;
}
