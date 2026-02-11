package com.synergisticit.stock_pattern_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.service.annotation.GetExchange;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "stock_pattern")
public class StockPattern {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    private String symbol;
    private String patternType;
    private String timeFrame;
    private LocalDateTime detectedAt;
}
