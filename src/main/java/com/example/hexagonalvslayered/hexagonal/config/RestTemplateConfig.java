package com.example.hexagonalvslayered.hexagonal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {
    
    @Value("${spring.rest.notification-service-url}")
    private String notificationServiceUrl;
    
    @Value("${spring.rest.events-endpoint}")
    private String eventsEndpoint;
    
    public String getNotificationServiceUrl() {
        return notificationServiceUrl;
    }
    
    public String getEventsEndpoint() {
        return eventsEndpoint;
    }
    
    public String getFullEventUrl() {
        return notificationServiceUrl + eventsEndpoint;
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .build();
    }
} 