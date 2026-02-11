package com.synergisticit.stock_pattern_service.exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandlers {
    @ExceptionHandler(InvalidResponseException.class)
    public Map<String,String> handleInvalidResponseException(InvalidResponseException ex){
        Map<String,String> map = new HashMap<>();
        map.put("Error while fetching data",ex.getMessage());
        return map;
    }
}
