package com.synergisticit.stock_pattern_service.controller;

import com.synergisticit.stock_pattern_service.service.PatternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patterns")
public class PatternController {

    @Autowired
    PatternService patternService;

    @GetMapping("/")
    public List<Map<String,Object>> getAllStockPattern(){
       return patternService.getMajorStockPatterns();
    }

    @PostMapping("/analyze/{symbol}/{timeFrame}")
    public ResponseEntity<Map<String,Object>> getStockPattern(@PathVariable("symbol") String symbol,
                                                              @PathVariable("timeFrame") String timeFrame){

            Map<String, Object> response = patternService.getStockPattern(symbol, timeFrame);
            return ResponseEntity.ok(response);
    }
}
