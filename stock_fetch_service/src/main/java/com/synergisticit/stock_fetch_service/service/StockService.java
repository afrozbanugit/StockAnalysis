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

import static com.synergisticit.stock_fetch_service.mapper.MapToStockDataDto.mapToDto;

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
    public StockDataDto fetchStockData(String symbol){
        Map<String,Object> responseMap = fetchStockDataFromFinnhub(symbol);
        StockData stockData =stockRepository.findBySymbol(symbol);
        if(stockData == null){
            stockData = new StockData();
            stockData.setSymbol(symbol.toUpperCase());
        }
        mapResponseToStockData(responseMap, stockData);
        stockRepository.save(stockData);
        return mapToDto(stockData);
    }

    private StockData mapResponseToStockData(Map<String, Object> responseMap, StockData stockData) {

        BigDecimal currentPrice = parseBigDecimal(responseMap.get("c"));
        BigDecimal changePrice = parseBigDecimal(responseMap.get("d"));
        Instant i = Instant.ofEpochSecond((Integer)responseMap.get("t"));
        BigDecimal lowPrice = parseBigDecimal(responseMap.get("l"));
        BigDecimal highPrice = parseBigDecimal(responseMap.get("h"));
        BigDecimal percentChange = parseBigDecimal(responseMap.get("dp"));
        BigDecimal openPrice = parseBigDecimal(responseMap.get("o"));
        BigDecimal prevClose = parseBigDecimal(responseMap.get("pc"));
        stockData.setCurrentPrice(currentPrice);
        stockData.setChange(changePrice);
        stockData.setFetchedDate(LocalDateTime.ofInstant(i, ZoneId.of("America/Los_Angeles")));
        stockData.setLowPrice(lowPrice);
        stockData.setHighPrice(highPrice);
        stockData.setOpenPrice(openPrice);
        stockData.setPrevClose(prevClose);
        stockData.setPercentChange(percentChange);
        return stockData;
    }

    @RateLimiter(name = "finnhubApi",fallbackMethod = "rateLimitFallBack")
    public Map<String,Object> fetchStockDataFromFinnhub(String symbol){
        String url = String.format("https://finnhub.io/api/v1/quote?symbol=%s&token=%s",
                symbol.toUpperCase(),apiKey);
        logger.info("url "+url);
        Map<String, Object> response = new HashMap<>();
        try {
            response = restTemplate.getForObject(url, Map.class);
            System.out.println("response " + response);
            return response;
        }catch(InvalidSymbolException e){
            logger.error("Invalid input symbol" +e.getMessage());
            throw new InvalidSymbolException("Invalid Symbol");
        }
        catch(Exception e){
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

    private BigDecimal parseBigDecimal(Object value){
        try{
            if(value==null) return BigDecimal.ZERO;
            return new BigDecimal(value.toString());
        }catch (Exception e){
            return BigDecimal.ZERO;
        }
    }
    @Cacheable(value = "stock_data",key = "#symbol")
    public StockDataDto fetchStockDataFromDb(String symbol) {
        StockData stockData = stockRepository.findBySymbol(symbol);
        return mapToDto(stockData);
    }
}
