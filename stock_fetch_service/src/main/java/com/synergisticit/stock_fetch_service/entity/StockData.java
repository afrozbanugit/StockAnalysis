package com.synergisticit.stock_fetch_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "stock_data")
public class StockData{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String symbol;
    private BigDecimal currentPrice;
    @Column(name="changes")// Bcos change is a mysql keyword!
    private BigDecimal change;
    private BigDecimal lowPrice;
    private BigDecimal highPrice;
    private BigDecimal percentChange;
    private BigDecimal prevClose;
    private BigDecimal openPrice;
    private LocalDateTime fetchedDate;
}
/*
c → Current price
d → Change (absolute value)
dp → Percent change from previous close
h → High price (highest today)
l → Low price (lowest today)
pc → Previous close price
o → Open price (today’s opening price)
t → Timestamp (Unix epoch seconds)*/
