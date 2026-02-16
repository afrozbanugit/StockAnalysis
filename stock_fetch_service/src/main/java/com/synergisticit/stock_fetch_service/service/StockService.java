package com.synergisticit.stock_fetch_service.service;

import com.synergisticit.stock_fetch_service.dto.StockDataDto;
import com.synergisticit.stock_fetch_service.entity.StockData;
import com.synergisticit.stock_fetch_service.exception.CustomBusinessException;
import com.synergisticit.stock_fetch_service.exception.InvalidSymbolException;
import com.synergisticit.stock_fetch_service.repository.StockRepository;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.synergisticit.stock_fetch_service.mapper.MapToStockDataDto.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.synergisticit.stock_fetch_service.mapper.MapToStockDataDto.mapResponseToStockData;
import static com.synergisticit.stock_fetch_service.mapper.MapToStockDataDto.mapToDto;
import static com.synergisticit.stock_fetch_service.mapper.MapToStockDataResponseDto.mapToResponseMap;

@Service
public class StockService {
    @Autowired
    StockRepository stockRepository;
    RestTemplate restTemplate = new RestTemplate();
    ConcurrentHashMap<String, StockData> chm = new ConcurrentHashMap<>();
    @Value("${finnhub.api.key}")
    String apiKey;

    private final static Logger logger = LoggerFactory.getLogger(StockService.class);

    @Cacheable(value = "stock_data",key ="#symbol" )
    public Map<String,Object> fetchStockData(String symbol){
        Map<String,Object> finnhubData = fetchStockDataFromFinnhub(symbol);

            StockData stockData = stockRepository.findBySymbol(symbol);
            if (stockData == null) {
                stockData = new StockData();
                stockData.setSymbol(symbol.toUpperCase());
            }
            mapResponseToStockData(finnhubData, stockData);
            stockRepository.save(stockData);
        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("id",stockData.getId());
        responseMap.put("symbol",symbol);
        responseMap.put("price",finnhubData.get("c"));
        responseMap.put("change",finnhubData.get("d"));
        responseMap.put("fetchedAt",stockData.getFetchedDate());
        return responseMap;
    }

    @RateLimiter(name = "finnhubApi",fallbackMethod = "rateLimitFallBack")
    public Map<String,Object> fetchStockDataFromFinnhub(String symbol){
        String url = String.format("https://finnhub.io/api/v1/quote?symbol=%s&token=%s",
                symbol.toUpperCase(),apiKey);
        logger.info("url "+url);
        Map<String, Object> response = new HashMap<>();
        try {
            response = restTemplate.getForObject(url, Map.class);
            System.out.println("Finnhub response " + response);
            if(response.get("dp")==null && response.get("d")==null ){
                logger.error("Invalid Symbol passed");
                throw new InvalidSymbolException(symbol);
            }
            return response;
        }
        catch(RestClientException e){
            logger.error("Error while fetching data from finnhub" +e.getMessage());
            throw new CustomBusinessException(e.getMessage());
        }
    }

    public Map<String,Object> rateLimitFallBack(RequestNotPermitted ex){
        logger.error("Rate limit reached");
        throw new CustomBusinessException(ex.getMessage());
    }

    @CachePut(value = "stock_data", key = "#symbol")
    public StockDataDto refreshStockData(String symbol) {
        Map<String,Object> responseMap = fetchStockDataFromFinnhub(symbol);
        StockData stockData =stockRepository.findBySymbol(symbol);
        stockData = mapResponseToStockData(responseMap,stockData);
        stockRepository.save(stockData);
        return mapToDto(stockData);
    }
    
   /* @Scheduled(cron = "${cron.job.expression}")
    private void refreshPopularStockData(){
        List<String> mostSearchedSymbols = List.of("AAPL","MSFT","AMZN");
        logger.info("Scheduled task to get stock prices");
        mostSearchedSymbols.stream().forEach(this::fetchStockData);
    }*/

    @Scheduled(cron = "${cron.job.expression}")
    public void refreshAllStockData(){
        List<StockData> stockDataList = stockRepository.findAll();
        logger.info("Scheduled task to get stock prices");
        System.out.println("stock data list from db "+ stockDataList);
        stockDataList.stream()
                        .map(StockData::getSymbol)
                        .forEach(this::refreshStockData);
    }

   // @Cacheable(value = "stock_data",key = "#symbol")
    public Map<String,Object> fetchStockDataFromDb(String symbol) {
       StockData stockData = stockRepository.findBySymbol(symbol);
        System.out.println("Is stockdata available in db? "+stockData);
        if(stockData ==null){
            Map<String,Object> finnhubData= fetchStockDataFromFinnhub(symbol);
            return finnhubData;
        }else{
            return mapToResponseMap(stockData);
        }
    }
}
