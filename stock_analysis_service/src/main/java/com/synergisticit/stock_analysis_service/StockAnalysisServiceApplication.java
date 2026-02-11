package com.synergisticit.stock_analysis_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class StockAnalysisServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockAnalysisServiceApplication.class, args);
	}
	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate();
	}

}
