package com.synergisticit.stock_fetch_service.repository;

import com.synergisticit.stock_fetch_service.entity.StockData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<StockData,Long> {

    StockData findBySymbol(String symbol);
}
