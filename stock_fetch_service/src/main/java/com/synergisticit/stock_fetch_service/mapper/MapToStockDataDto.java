package com.synergisticit.stock_fetch_service.mapper;

import com.synergisticit.stock_fetch_service.dto.StockDataDto;
import com.synergisticit.stock_fetch_service.entity.StockData;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

public class MapToStockDataDto {

    public static StockDataDto mapToDto(StockData stockData){
        StockDataDto stockDataDto = new StockDataDto();
        stockDataDto.setCurrentPrice(stockData.getCurrentPrice());
        stockDataDto.setChange(stockData.getChange());
        stockDataDto.setFetchedDate(stockData.getFetchedDate());
        stockDataDto.setSymbol(stockData.getSymbol());
        stockDataDto.setLowPrice(stockData.getLowPrice());
        stockDataDto.setHighPrice(stockData.getHighPrice());
        stockDataDto.setPercentChange(stockData.getPercentChange());
        stockDataDto.setPrevClose(stockData.getPrevClose());
        return stockDataDto;
    }

    public static StockData mapResponseToStockData(Map<String, Object> responseMap, StockData stockData) {

        BigDecimal currentPrice = parseBigDecimal(responseMap.get("c"));
        BigDecimal changePrice = parseBigDecimal(responseMap.get("d"));
        Instant i = Instant.ofEpochSecond((Integer)responseMap.get("t"));
        BigDecimal lowPrice = parseBigDecimal(responseMap.get("l"));
        BigDecimal highPrice = parseBigDecimal(responseMap.get("h"));
        BigDecimal percentChange = parseBigDecimal(responseMap.get("dp"));
        BigDecimal openPrice = parseBigDecimal(responseMap.get("o"));
        BigDecimal prevClose = parseBigDecimal(responseMap.get("pc"));
        stockData.setCurrentPrice(currentPrice);
        stockData.setChange(changePrice);
        stockData.setFetchedDate(LocalDateTime.ofInstant(i, ZoneId.of("America/Los_Angeles")));
        stockData.setLowPrice(lowPrice);
        stockData.setHighPrice(highPrice);
        stockData.setOpenPrice(openPrice);
        stockData.setPrevClose(prevClose);
        stockData.setPercentChange(percentChange);
        return stockData;
    }

    private static BigDecimal parseBigDecimal(Object value){
        try{
            if(value==null) return BigDecimal.ZERO;
            return new BigDecimal(value.toString());
        }catch (Exception e){
            return BigDecimal.ZERO;
        }
    }
}
