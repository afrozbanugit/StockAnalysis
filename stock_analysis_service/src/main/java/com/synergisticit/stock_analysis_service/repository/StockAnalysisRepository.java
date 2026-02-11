package com.synergisticit.stock_analysis_service.repository;


import com.synergisticit.stock_analysis_service.entity.StockMetrics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockAnalysisRepository extends JpaRepository<StockMetrics,Long> {
    StockMetrics findBySymbol(String symbol);
}
