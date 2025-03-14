package com.example.hexagonalvslayered.layered.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.hexagonalvslayered.layered.repository")
@EnableTransactionManagement
public class LayeredConfig {
} 