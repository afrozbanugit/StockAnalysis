package com.synergisticit.stock_fetch_service.mapper;

import com.synergisticit.stock_fetch_service.dto.StockDataDto;
import com.synergisticit.stock_fetch_service.entity.StockData;

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
}
