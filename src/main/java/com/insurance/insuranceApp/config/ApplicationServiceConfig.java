package com.insurance.insuranceApp.config;

import com.insurance.insuranceApp.services.implementations.ManageProductApplicationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationServiceConfig {
    @Bean
    public ManageProductApplicationService manageProductApplicationService(){return new ManageProductApplicationService();}
}
