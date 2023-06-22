package com.tamayo.gcash.exam.dto;

import lombok.Data;

@Data
public class ErrorResponseDTO {
    private String error;

    public ErrorResponseDTO(String error) {
        this.error = error;
    }
}
