package com.synergisticit.stock_api_gateway.service;

import com.synergisticit.stock_api_gateway.dto.CombinedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class StockFlowService {
    @Autowired
    WebClient webClient;

    @Value("${services.fetcher-service.uri}")
    private String fetcherUrl;
    @Value("${services.analysis-service.uri}")
    private String analysisUrl;
    @Value("${services.pattern-service.uri}")
    private String patternUrl;

    public Mono<Map<String,Object>> getFullStockAnalysis(String symbol) {

        Mono<Map<String,Object>> fetcherMono= getStockData(symbol);

        Mono<Map<String,Object>> metricsMono= getStockMetricsData(symbol);
        Mono<Map<String,Object>> patternMono= getStockPatternData(symbol);
        //Mono.zip(fetcher,metrics) is non-blocking and concurrent, but we want fetcher to be called and then analysismetrics.
         /*Mono<CombinedResponse> combinedResponse= Mono.zip(fetcherMono,metricsMono,patternMono).map(t ->{
            System.out.println(t.getT3());
            CombinedResponse dto = new CombinedResponse(t.getT1(),t.getT2(),t.getT3());
            return dto;
        });*/
        Mono<Map<String,Object>> combinedResponse = getStockData(symbol)
                .flatMap(fetcherData -> getStockMetricsData(symbol)
                        .flatMap(metricsData -> getStockPatternData(symbol)
                        .map(patternData -> combine(fetcherData,metricsData,patternData))
                        )
                );

        return combinedResponse;
    }

    private Mono<Map<String, Object>> getStockPatternData(String symbol) {
        String baseUrl = patternUrl+"/patterns/analyze/{symbol}/{timeFrame}";
        return webClient.post()
                .uri(baseUrl,symbol,"1D")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {
                })
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(ex -> {
                    System.out.println(ex.getMessage());
                    return Mono.just(Map.of("Pattern Analysis Service unavailable", ""));
                });
    }

    private Mono<Map<String, Object>> getStockMetricsData(String symbol) {
        String baseUrl = analysisUrl+"/analysis/metrics/{symbol}";
        return webClient.get()
                .uri(baseUrl,symbol)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {
                })
                .timeout(Duration.ofSeconds(4))
                .onErrorResume(ex -> {
                    System.out.println(ex.getMessage());
                    System.out.println(ex.getClass());
                  //  ex.printStackTrace();
                    if(ex instanceof WebClientResponseException){
                        return Mono.just(Map.of("Invalid Symbol ",symbol));
                    }
                   return Mono.just(Map.of("Stock Analysis Service unavailable",""));
                });
    }



    private Mono<Map<String,Object>> getStockData(String symbol) {

        String url = fetcherUrl +"/stocks/fetch/{symbol}";
        return webClient.get()
                .uri(url,symbol)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {
                })
                .timeout(Duration.ofSeconds(4))
                .onErrorResume(ex -> {
                    if(ex instanceof WebClientResponseException){
                        return Mono.just(Map.of("Invalid Symbol ",symbol));
                    }
                    return Mono.just(Map.of("Fetcher Service Unavailable",""));
                });
        
    }

    public Map<String, Object> combine(
            Map<String, Object> stock,
            Map<String, Object> metrics,
            Map<String, Object> analysis) {

        Map<String, Object> result = new HashMap<>();

        result.put("stockData", stock);
        result.put("metricsData", metrics);
        result.put("analysisData", analysis);

        return result;
    }
}
