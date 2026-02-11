package com.synergisticit.stock_analysis_service.exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvalidResponseExceptionHandler {
    @ExceptionHandler(InvalidResponseException.class)
    public String handleInvalidResponse(InvalidResponseException ex){
        return "Exception while fetching data from Fetch service " + ex.getMessage();
    }

}
