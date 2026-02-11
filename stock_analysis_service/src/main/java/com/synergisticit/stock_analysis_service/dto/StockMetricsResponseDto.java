package com.synergisticit.stock_analysis_service.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class StockMetricsResponseDto {

    private BigDecimal averagePrice;
    private BigDecimal dailyChangePercent;
}
