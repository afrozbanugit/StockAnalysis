package com.synergisticit.stock_analysis_service.service;

import com.synergisticit.stock_analysis_service.controller.StockAnalysisController;
import com.synergisticit.stock_analysis_service.dto.StockDataDto;
import com.synergisticit.stock_analysis_service.entity.StockMetrics;
import com.synergisticit.stock_analysis_service.repository.StockAnalysisRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class StockAnalysisService {

    @Autowired
    StockAnalysisRepository stockAnalysisRepository;
    @Autowired
    RestTemplate restTemplate;
    @Value("${service.fetcher.url}")
    private String stock_fetcher_service_url;

    private final static String fetcher_service = "fetcher-service";
    private final static Logger logger = LoggerFactory.getLogger(StockAnalysisService.class);


    private BigDecimal calculateAveragePrice(StockDataDto stockDataDto) {
        BigDecimal average = BigDecimal.ZERO;
        try{
            average = (stockDataDto.getHighPrice().add(stockDataDto.getLowPrice()))
                .divide(BigDecimal.valueOf(2));
            return average;
        }catch (ArithmeticException e){
            System.out.println("Arithmetic exception while calculating average "+ e.getMessage());
            return average;
        }
    }

    public BigDecimal getPercentChangeInPrice(StockDataDto stockDataDto) {
         return stockDataDto.getPercentChange();

    }
    @CircuitBreaker(name=fetcher_service,fallbackMethod = "fetchService_fallBack")
    public Map<String, Object> calculateMetrics(String symbol) {
        String url = stock_fetcher_service_url + "/fetch/stockData/"+ symbol;
        System.out.println("url "+url);
        StockDataDto stockDataDto = restTemplate.getForObject(url, StockDataDto.class);
        System.out.println("stock data dto "+ stockDataDto);
        BigDecimal avg = BigDecimal.ZERO;
        BigDecimal percentChange = BigDecimal.ZERO;
        Map<String,Object> map = new HashMap<>();
        if(stockDataDto != null) {
            avg = calculateAveragePrice(stockDataDto);
            percentChange = getPercentChangeInPrice(stockDataDto);
            saveInDatabase(stockDataDto,map);
        }
        map.put("averagePrice",avg);
        map.put("percentChange",percentChange);
        return map;
    }

    public Map<String, BigDecimal> fetchService_fallBack(Exception ex){
        logger.info("In Fall Back method");
        Map<String,BigDecimal> map = new HashMap<>();
        map.put("Service_Unavailable",BigDecimal.ZERO);
        return map;
    }

    private void saveInDatabase(StockDataDto stockDataDto, Map<String, Object> map) {
        StockMetrics stockMetrics =stockAnalysisRepository.findBySymbol(stockDataDto.getSymbol());
        if(stockMetrics == null){
            System.out.println("New Symbol");
            stockMetrics = new StockMetrics();
            stockMetrics.setSymbol(stockDataDto.getSymbol());
        }
        stockMetrics.setPrice(stockDataDto.getCurrentPrice());
        stockMetrics.setAveragePrice((BigDecimal) map.get("averagePrice"));
        stockMetrics.setPercentChange((BigDecimal)map.get("percentChange"));
        stockAnalysisRepository.save(stockMetrics);
    }
}
