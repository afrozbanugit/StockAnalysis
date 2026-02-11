package com.synergisticit.stock_pattern_service.exceptions;

public class InvalidResponseException extends RuntimeException {
    public InvalidResponseException(String message) {
        super(message);
    }
}
