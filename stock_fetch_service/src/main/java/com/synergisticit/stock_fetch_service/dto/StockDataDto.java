package com.synergisticit.stock_fetch_service.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StockDataDto implements Serializable {

    private String symbol;
    private BigDecimal currentPrice;
    private BigDecimal change;
    private BigDecimal lowPrice;
    private BigDecimal highPrice;
    private BigDecimal percentChange;
    private BigDecimal prevClose;
    private BigDecimal openPrice;
    private LocalDateTime fetchedDate;
}
