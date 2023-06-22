package com.tamayo.gcash.exam.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.tamayo.gcash.exam.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class ErrorHandlerController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleJsonMappingException(JsonMappingException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("request has empty body");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponseDTO> handleDefaultException(Throwable ex) {
        FieldError fldError = ((BindException) ex).getFieldError();
        String fld = fldError.getField().toUpperCase();
        String msg = fldError.getDefaultMessage();
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(String.format("%s: %s", fld,msg));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
