package com.synergisticit.stock_fetch_service.exception;

public class CustomBusinessException extends RuntimeException {
    public CustomBusinessException(String message) {
        super(message);
    }
}
