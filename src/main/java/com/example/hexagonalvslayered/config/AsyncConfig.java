package com.example.hexagonalvslayered.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 비동기 이벤트 처리를 위한 설정 클래스
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    // 필요한 경우 여기에 ThreadPoolTaskExecutor 등의 설정을 추가할 수 있습니다.
} 