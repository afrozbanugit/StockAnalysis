package com.synergisticit.stock_analysis_service.exceptions;

public class InvalidSymbolException extends RuntimeException {

    String symbol;
    public InvalidSymbolException(String symbol) {
        super(symbol);
        this.symbol=symbol;
    }
    public String getSymbol(){
        return this.symbol;
    }

}
