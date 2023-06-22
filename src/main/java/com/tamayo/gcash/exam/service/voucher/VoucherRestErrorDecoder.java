package com.tamayo.gcash.exam.service.voucher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamayo.gcash.exam.dto.ErrorResponseDTO;
import feign.Response;
import feign.codec.ErrorDecoder;
import io.micrometer.core.instrument.util.IOUtils;
import org.apache.commons.codec.Charsets;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerErrorException;


public class VoucherRestErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        try {
            HttpStatus responseStatus = HttpStatus.valueOf(response.status());
            if (responseStatus.is5xxServerError()) {
                return new ServerErrorException("Server error in Voucher API",null);
            } else if (responseStatus.is4xxClientError()) {
                String json = IOUtils.toString(response.body().asInputStream(), Charsets.UTF_8);
                ErrorResponseDTO error = new ObjectMapper().readValue(json, ErrorResponseDTO.class);
                 throw new RuntimeException(error.getError());
            } else {
                return new Exception();
            }
        } catch (Exception e) {
            return new Exception(e.getMessage());
        }
    }
}
