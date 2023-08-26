package com.example.BusinessProfileManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableCaching
@EnableCircuitBreaker
public class BusinessProfileManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusinessProfileManagementApplication.class, args);
	}

}
