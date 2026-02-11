package com.synergisticit.stock_fetch_service.controller;

import com.synergisticit.stock_fetch_service.dto.StockDataDto;
import com.synergisticit.stock_fetch_service.dto.StockDataResponseDto;
import com.synergisticit.stock_fetch_service.entity.StockData;
import com.synergisticit.stock_fetch_service.mapper.MapToStockDataResponseDto;
import com.synergisticit.stock_fetch_service.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    StockService stockService;

    @GetMapping("/fetch/{symbol}")
    public StockDataResponseDto fetchStockData(@PathVariable String symbol){
        StockDataDto dto = stockService.fetchStockData(symbol);
        return MapToStockDataResponseDto.mapToResponseDto(dto);
    }

    @GetMapping("/fetch/stockData/{symbol}")
    public StockDataDto fetchStockDataFromDb(@PathVariable String symbol){
        return stockService.fetchStockDataFromDb(symbol);
    }
}
