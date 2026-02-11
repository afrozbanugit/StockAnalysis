package com.synergisticit.stock_api_gateway.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.Map;


public class CombinedResponse {

    Map<String,Object> stockData;
    Map<String,Object> analysisData;
    Map<String,Object> pattern;

    public CombinedResponse(Map<String,Object> stockData,
                            Map<String,Object> analysisData,
                            Map<String,Object> patternData){
        this.stockData=stockData;
        this.analysisData=analysisData;
        this.pattern=patternData;
    }

    public Map<String, Object> getStockData() {
        return stockData;
    }

    public Map<String, Object> getAnalysisData() {
        return analysisData;
    }

    public Map<String, Object> getPattern() {
        return pattern;
    }

    public void setStockData(Map<String, Object> stockData) {
        this.stockData = stockData;
    }

    public void setAnalysisData(Map<String, Object> analysisData) {
        this.analysisData = analysisData;
    }

    public void setPattern(Map<String, Object> pattern) {
        this.pattern = pattern;
    }

    @Override
    public String toString() {
        return
                "stockData=" + stockData +
                ", analysisData=" + analysisData +
                ", pattern=" + pattern +
                '}';
    }
}
