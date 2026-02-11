package com.synergisticit.stock_pattern_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class StockPatternServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockPatternServiceApplication.class, args);
	}
	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
