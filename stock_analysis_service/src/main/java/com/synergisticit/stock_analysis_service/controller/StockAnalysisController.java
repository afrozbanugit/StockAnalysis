package com.synergisticit.stock_analysis_service.controller;

import com.synergisticit.stock_analysis_service.dto.StockDataDto;
import com.synergisticit.stock_analysis_service.dto.StockMetricsResponseDto;
import com.synergisticit.stock_analysis_service.service.StockAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.synergisticit.stock_analysis_service.mapper.MapToStockMetricsResponseDto.mapToStockMetricsResponseDto;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/analysis")
public class StockAnalysisController {

    @Autowired
    StockAnalysisService stockAnalysisService;

    private final static Logger logger = LoggerFactory.getLogger(StockAnalysisController.class);

    @GetMapping("/metrics/{symbol}")
    public Map<String,Object> calculateMetrics(@PathVariable String symbol){
        logger.info("Retrieve Stocks Metrics");
        Map<String,Object> map = stockAnalysisService.calculateMetrics(symbol);
        return map;
     //   return mapToStockMetricsResponseDto(map);
    }

}
