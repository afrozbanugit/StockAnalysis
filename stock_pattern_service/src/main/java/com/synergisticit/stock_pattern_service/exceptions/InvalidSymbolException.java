package com.synergisticit.stock_pattern_service.exceptions;

import lombok.Getter;

@Getter
public class InvalidSymbolException extends RuntimeException {
    private String symbol;
    public InvalidSymbolException(String symbol) {
        super(symbol);
        this.symbol = symbol;
    }
}
