package com.synergisticit.stock_fetch_service.mapper;

import com.synergisticit.stock_fetch_service.dto.StockDataDto;
import com.synergisticit.stock_fetch_service.dto.StockDataResponseDto;
import com.synergisticit.stock_fetch_service.entity.StockData;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class MapToStockDataResponseDto {

    public static StockDataResponseDto mapToResponseDto(StockDataDto stockDataDto){
        StockDataResponseDto stockDataResponseDto = new StockDataResponseDto();
        stockDataResponseDto.setCurrentPrice(stockDataDto.getCurrentPrice());
        stockDataResponseDto.setChange(stockDataDto.getChange());
        stockDataResponseDto.setFetchedDate(stockDataDto.getFetchedDate());
        stockDataResponseDto.setSymbol(stockDataDto.getSymbol());
        return stockDataResponseDto;
    }

    public static Map<String,Object> mapToResponseMap(StockData stockData){
        Map<String,Object> map = new HashMap<>();
        map.put("c",stockData.getCurrentPrice());
        map.put("d",stockData.getChange());
        map.put("t",stockData.getFetchedDate());
        map.put("l",stockData.getLowPrice());
        map.put("h",stockData.getHighPrice());
        map.put("dp",stockData.getPercentChange());
        map.put("o",stockData.getOpenPrice());
        map.put("pc",stockData.getPrevClose());
        return map;
    }
}
