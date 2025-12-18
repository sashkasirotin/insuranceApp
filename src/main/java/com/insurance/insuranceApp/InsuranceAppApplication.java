package com.insurance.insuranceApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class InsuranceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(InsuranceAppApplication.class, args);
	}

}
