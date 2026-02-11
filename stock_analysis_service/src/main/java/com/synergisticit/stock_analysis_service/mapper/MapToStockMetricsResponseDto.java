package com.synergisticit.stock_analysis_service.mapper;

import com.synergisticit.stock_analysis_service.dto.StockDataDto;
import com.synergisticit.stock_analysis_service.dto.StockMetricsResponseDto;
import com.synergisticit.stock_analysis_service.entity.StockMetrics;

import java.math.BigDecimal;
import java.util.Map;

public class MapToStockMetricsResponseDto {

    public static StockMetricsResponseDto mapToStockMetricsResponseDto(Map<String, BigDecimal> map){
        StockMetricsResponseDto stockDataDto = new StockMetricsResponseDto();
        stockDataDto.setAveragePrice(map.get("averagePrice"));
        stockDataDto.setDailyChangePercent(map.get("percentChange"));
        return stockDataDto;
    }
}
