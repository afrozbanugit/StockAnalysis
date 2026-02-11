package com.synergisticit.stock_analysis_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="stock_metrics")
public class StockMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String symbol;
    private BigDecimal price;
    private BigDecimal averagePrice;
    private BigDecimal percentChange;
}
