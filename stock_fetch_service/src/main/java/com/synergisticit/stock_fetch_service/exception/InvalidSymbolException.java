package com.synergisticit.stock_fetch_service.exception;

public class InvalidSymbolException extends RuntimeException {
    public InvalidSymbolException(String message) {
        super(message);
    }
}
