package com.synergisticit.stock_analysis_service.exceptions;

public class InvalidResponseException extends RuntimeException {
    public InvalidResponseException(String message) {
        super(message);
    }
}
