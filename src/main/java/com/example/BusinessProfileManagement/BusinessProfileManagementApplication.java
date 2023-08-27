package com.example.BusinessProfileManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableCaching
@EnableCircuitBreaker
public class BusinessProfileManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusinessProfileManagementApplication.class, args);
	}

}
