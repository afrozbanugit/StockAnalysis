package com.synergisticit.stock_fetch_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = InvalidSymbolException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidSymbolInput(InvalidSymbolException ex){
        ErrorResponseDto error = new ErrorResponseDto("Invalid Symbol",ex.getSymbol());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);

    }

    @ExceptionHandler(value = CustomBusinessException.class)
    public ResponseEntity<ErrorResponse> handleCustomBusinessException(CustomBusinessException ex){
        ErrorResponse error = new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR,ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

}
