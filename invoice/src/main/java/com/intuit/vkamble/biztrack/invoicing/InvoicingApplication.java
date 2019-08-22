package com.intuit.vkamble.biztrack.invoicing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCircuitBreaker
public class InvoicingApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoicingApplication.class, args);
	}
}
