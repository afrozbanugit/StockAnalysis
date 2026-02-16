package com.synergisticit.stock_pattern_service.service;

import com.synergisticit.stock_pattern_service.entity.StockPattern;
import com.synergisticit.stock_pattern_service.exceptions.InvalidResponseException;
import com.synergisticit.stock_pattern_service.exceptions.InvalidSymbolException;
import com.synergisticit.stock_pattern_service.exceptions.ServiceUnavailableException;
import com.synergisticit.stock_pattern_service.repository.PatternRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PatternService {

    @Autowired
    PatternRepository patternRepository;
    @Autowired
    RestTemplate restTemplate;
    @Value("${analysis.service.url}")
    private String analysisServiceUrl;

    @CircuitBreaker(name="analysis_service",fallbackMethod = "analysisService_fallBack")
    @Cacheable(cacheNames = "stock_pattern",key="#symbol")
    public Map<String, Object> getStockPattern(String symbol, String timeFrame) {
        String url = analysisServiceUrl +"metrics/"+symbol;
        System.out.println("url "+ url);
        Map<String,Object> responseMap = new HashMap<>();

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            System.out.println("Analysis response map "+ response.getBody());
            Map analysisMap = response.getBody();
            if (analysisMap != null && !analysisMap.isEmpty()) {
                double percentChange = (Double)analysisMap.get("percentChange");
                System.out.println("percentChange "+percentChange);
                String trend="NoChange";
                trend= percentChange > 0.0?"Up-Trend":percentChange<0.0?"Down-Trend":"No-Change";
                saveToDB(analysisMap,symbol,trend);
                responseMap.put("patternType",trend);
                responseMap.put("symbol",symbol);
                responseMap.put("timeFrame","1D");
                responseMap.put("detectedAt",LocalDateTime.now());
                return responseMap;
            }
        return responseMap;
    }

    public Map<String,Object> analysisService_fallBack(String symbol, String timeFrame,Exception e){
        System.out.println("Analysis service returned exception "+ e.getMessage() +" of type "+ e.getClass());
        if(e instanceof HttpClientErrorException | e instanceof HttpServerErrorException){
            if(((HttpStatusCodeException) e).getStatusCode().equals(HttpStatus.NOT_FOUND)){
                throw new InvalidSymbolException(symbol);
            }
            throw new InvalidResponseException(e.getMessage());
        }
        if(e instanceof RestClientException){
            throw new ServiceUnavailableException(e.getMessage());
        }
        Map<String,Object> map = new HashMap<>();
        map.put("Pattern service returned error",e.getMessage());
        return map;
    }

    private void saveToDB(Map<String, Object> map,String symbol,String trend) {
        StockPattern stockPattern =patternRepository.findBySymbol(symbol);
        System.out.println("Save in Repository");
        if(stockPattern == null){
            stockPattern = new StockPattern();
            stockPattern.setSymbol(symbol);
        }
        stockPattern.setPatternType(trend);
        stockPattern.setDetectedAt(LocalDateTime.now());
        stockPattern.setTimeFrame("1D");
        patternRepository.save(stockPattern);
    }

    @Cacheable(cacheNames = "stock_patterns")
    @Scheduled(cron = "0 0 */1 * * MON-FRI")
    public List<Map<String,Object>> getMajorStockPatterns() throws InvalidResponseException{
        List<String> majorStockList = List.of("AAPL","AMZN","MSFT");
        String timeFrame = "1D";
        return majorStockList.stream()
                .map((symbol) -> getStockPattern(symbol,timeFrame))
                .toList();
    }
}
