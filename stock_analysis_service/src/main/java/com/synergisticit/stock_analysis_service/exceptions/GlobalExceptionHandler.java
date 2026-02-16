package com.synergisticit.stock_analysis_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidResponseException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidResponse(InvalidResponseException ex){
        ErrorResponseDto error = new ErrorResponseDto("Error Fetching Data from Fetcher Service",ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(InvalidSymbolException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidSymbolException(InvalidSymbolException ex){
        ErrorResponseDto error = new ErrorResponseDto("Invalid Symbol",ex.getSymbol());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

}
