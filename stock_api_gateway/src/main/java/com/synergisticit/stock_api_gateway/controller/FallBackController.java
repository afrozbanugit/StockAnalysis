package com.synergisticit.stock_api_gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallBackController {
    @GetMapping("/fetch")
    public Mono<Map<String,Object>> fallBack_FetcherService(){
        return Mono.just(Map.of("stockData","Stock Fetcher Service Unavailable"));
    }
    @GetMapping("/analysis")
    public Mono<Map<String,Object>> fallBack_AnalysisService(){
        return Mono.just(Map.of("analysis","Stock Analysis Service Unavailable"));
    }
    @GetMapping("/pattern")
    public Mono<Map<String,Object>> fallBack_PatternService(){
        return Mono.just(Map.of("pattern","Stock Pattern Service Unavailable"));
    }
}
