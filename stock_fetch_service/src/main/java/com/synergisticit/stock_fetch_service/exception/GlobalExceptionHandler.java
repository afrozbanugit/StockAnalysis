package com.synergisticit.stock_fetch_service.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = InvalidSymbolException.class)
    public String handleInvalidSymbolInput(InvalidSymbolException ex){
            return "Symbol not found. Please enter a valid Symbol " + ex.getMessage();
    }

    @ExceptionHandler(value = CustomBusinessException.class)
    public String handleCustomBusinessException(CustomBusinessException ex){
        return "Exception while fetching data from finnhub API " + ex.getMessage();
    }
}
