package com.synergisticit.stock_fetch_service.controller;

import com.synergisticit.stock_fetch_service.dto.StockDataDto;
import com.synergisticit.stock_fetch_service.dto.StockDataResponseDto;
import com.synergisticit.stock_fetch_service.entity.StockData;
import com.synergisticit.stock_fetch_service.mapper.MapToStockDataResponseDto;
import com.synergisticit.stock_fetch_service.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    StockService stockService;

    @GetMapping("/fetch/{symbol}")
    public ResponseEntity<Map<String,Object>> fetchStockData(@PathVariable String symbol){

        //    StockDataDto dto = stockService.fetchStockData(symbol);
        Map<String,Object> stockMap = stockService.fetchStockData(symbol);
       //     return ResponseEntity.ok(MapToStockDataResponseDto.mapToResponseDto(dto));
        return ResponseEntity.ok(stockMap);
    }

    @GetMapping("/fetch/stockData/{symbol}")
    public ResponseEntity<Map<String,Object>> fetchStockDataFromDb(@PathVariable String symbol){
        System.out.println("Fetch from db");
      //  StockDataDto dto= stockService.fetchStockDataFromDb(symbol);
        Map<String,Object> responseMap = stockService.fetchStockDataFromDb(symbol);
        return ResponseEntity.ok(responseMap);
    }
}
