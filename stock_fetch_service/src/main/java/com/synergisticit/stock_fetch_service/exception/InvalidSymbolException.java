package com.synergisticit.stock_fetch_service.exception;

public class InvalidSymbolException extends RuntimeException {

    private final String symbol;
    public InvalidSymbolException(String symbol) {

        super("Invalid Symbol "+symbol);
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }
}
