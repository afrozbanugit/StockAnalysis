package com.synergisticit.stock_fetch_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StockDataResponseDto {

    private String symbol;
    private BigDecimal CurrentPrice;
    private BigDecimal change;
    private LocalDateTime fetchedDate;
}
