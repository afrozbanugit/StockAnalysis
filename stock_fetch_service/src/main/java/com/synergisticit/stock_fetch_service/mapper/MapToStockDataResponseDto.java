package com.synergisticit.stock_fetch_service.mapper;

import com.synergisticit.stock_fetch_service.dto.StockDataDto;
import com.synergisticit.stock_fetch_service.dto.StockDataResponseDto;
import com.synergisticit.stock_fetch_service.entity.StockData;

public class MapToStockDataResponseDto {

    public static StockDataResponseDto mapToResponseDto(StockDataDto stockDataDto){
        StockDataResponseDto stockDataResponseDto = new StockDataResponseDto();
        stockDataResponseDto.setCurrentPrice(stockDataDto.getCurrentPrice());
        stockDataResponseDto.setChange(stockDataDto.getChange());
        stockDataResponseDto.setFetchedDate(stockDataDto.getFetchedDate());
        stockDataResponseDto.setSymbol(stockDataDto.getSymbol());
        return stockDataResponseDto;
    }
}
