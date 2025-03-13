package com.example.hexagonalvslayered.hexagonal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.hexagonalvslayered.hexagonal.adapter.out.persistence")
@EnableTransactionManagement
public class HexagonalConfig {
    // 필요한 경우 추가 설정을 여기에 정의할 수 있습니다.
} 