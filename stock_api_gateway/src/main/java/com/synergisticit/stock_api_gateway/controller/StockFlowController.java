package com.synergisticit.stock_api_gateway.controller;

import com.synergisticit.stock_api_gateway.dto.CombinedResponse;
import com.synergisticit.stock_api_gateway.service.StockFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/stock")
public class StockFlowController {


    @Autowired
    StockFlowService stockFlowService;

    @PostMapping("/full-analysis/{symbol}")
    public Mono<CombinedResponse> fullStockAnalysis(@PathVariable String symbol){

        return stockFlowService.getFullStockAnalysis(symbol);
    }

}
