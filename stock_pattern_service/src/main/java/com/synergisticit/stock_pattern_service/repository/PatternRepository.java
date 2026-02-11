package com.synergisticit.stock_pattern_service.repository;

import com.synergisticit.stock_pattern_service.entity.StockPattern;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatternRepository extends JpaRepository<StockPattern,Long> {
    StockPattern findBySymbol(String symbol);
}
