package com.synergisticit.stock_api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class StockApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockApiGatewayApplication.class, args);
	}

}
