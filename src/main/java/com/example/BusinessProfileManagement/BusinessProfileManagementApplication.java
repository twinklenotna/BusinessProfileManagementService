package com.example.BusinessProfileManagement;

//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableCaching
public class BusinessProfileManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusinessProfileManagementApplication.class, args);
	}

}
