package com.synergisticit.stock_analysis_service.service;

import com.synergisticit.stock_analysis_service.controller.StockAnalysisController;
import com.synergisticit.stock_analysis_service.dto.StockDataDto;
import com.synergisticit.stock_analysis_service.entity.StockMetrics;
import com.synergisticit.stock_analysis_service.exceptions.ErrorResponseDto;
import com.synergisticit.stock_analysis_service.exceptions.InvalidResponseException;
import com.synergisticit.stock_analysis_service.exceptions.InvalidSymbolException;
import com.synergisticit.stock_analysis_service.repository.StockAnalysisRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.synergisticit.stock_analysis_service.mapper.MapToStockDataDto.mapToStockDataDto;

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
        System.out.println("stockdata "+stockDataDto);
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
     //   StockDataDto stockDataDto = restTemplate.getForObject(url, StockDataDto.class);

        ResponseEntity<Map> response   = restTemplate.getForEntity(url, Map.class);
      //    StockDataDto stockDataDto = (StockDataDto) response.getBody();
            Map<String,Object> fetcherMap = response.getBody();
            System.out.println("response from Fetcher Service " + fetcherMap);
            Map<String, Object> map = new HashMap<>();
            StockDataDto stockDataDto = mapToStockDataDto(fetcherMap,symbol);
            System.out.println("stockDatadto "+stockDataDto);
            BigDecimal avg = BigDecimal.ZERO;
            BigDecimal percentChange = BigDecimal.ZERO;
            avg = calculateAveragePrice(stockDataDto);
            percentChange = getPercentChangeInPrice(stockDataDto);
            System.out.printf("Avg is %s and Percentchange is %s ",avg,percentChange);
            saveInDatabase(stockDataDto, avg,percentChange);

            map.put("averagePrice", avg);
            map.put("percentChange", percentChange);
            return map;
    }

    public Map<String, BigDecimal> fetchService_fallBack(String symbol,Exception ex){
        logger.error("In Fall Back method due to  "+ ex.getMessage());
        Map<String,BigDecimal> map = new HashMap<>();
        logger.error("Exception --  "+ ex.getClass());
        if(ex instanceof HttpClientErrorException  e){
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                throw new InvalidSymbolException(symbol);
            }
            throw new InvalidResponseException(ex.getMessage());
        }
        if(ex instanceof RestClientException)
            map.put("Service_Unavailable "+ex.getMessage(),BigDecimal.ZERO);
        else
            map.put("Exception in Analysis Service "+ex.getMessage(),BigDecimal.ZERO);
        return map;
    }

    private void saveInDatabase(StockDataDto stockDataDto,BigDecimal average,BigDecimal percentChange ) {
        StockMetrics stockMetrics =stockAnalysisRepository.findBySymbol(stockDataDto.getSymbol());
        if(stockMetrics == null){
            System.out.println("New Symbol");
            stockMetrics = new StockMetrics();
            stockMetrics.setSymbol(stockDataDto.getSymbol());
        }
        stockMetrics.setPrice(stockDataDto.getCurrentPrice());
        stockMetrics.setAveragePrice(average);
        stockMetrics.setPercentChange(percentChange);
        stockAnalysisRepository.save(stockMetrics);
    }
}
