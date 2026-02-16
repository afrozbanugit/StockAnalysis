package com.synergisticit.stock_analysis_service.mapper;

import com.synergisticit.stock_analysis_service.dto.StockDataDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

public class MapToStockDataDto {

    public static StockDataDto mapToStockDataDto(Map<String,Object> responseMap,String symbol){

    BigDecimal currentPrice = parseBigDecimal(responseMap.get("c"));
    BigDecimal changePrice = parseBigDecimal(responseMap.get("d"));
    BigDecimal lowPrice = parseBigDecimal(responseMap.get("l"));
    BigDecimal highPrice = parseBigDecimal(responseMap.get("h"));
    BigDecimal percentChange = parseBigDecimal(responseMap.get("dp"));
    BigDecimal openPrice = parseBigDecimal(responseMap.get("o"));
    BigDecimal prevClose = parseBigDecimal(responseMap.get("pc"));
    StockDataDto stockData = new StockDataDto();
        System.out.println("set stockdatadto");
        stockData.setSymbol(symbol);
        stockData.setCurrentPrice(currentPrice);
        stockData.setChange(changePrice);
        stockData.setLowPrice(lowPrice);
        stockData.setHighPrice(highPrice);
        stockData.setOpenPrice(openPrice);
        stockData.setPrevClose(prevClose);
        stockData.setPercentChange(percentChange);
        return stockData;
    }

private static BigDecimal parseBigDecimal(Object value) {
    try {
        if (value == null) return BigDecimal.ZERO;
        return new BigDecimal(value.toString());
    } catch (Exception e) {
        return BigDecimal.ZERO;
    }
}
}
