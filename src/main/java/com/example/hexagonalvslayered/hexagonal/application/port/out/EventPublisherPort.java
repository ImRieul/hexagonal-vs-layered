package com.example.hexagonalvslayered.hexagonal.application.port.out;

import com.example.hexagonalvslayered.hexagonal.domain.event.DomainEvent;

/**
 * 도메인 이벤트를 발행하기 위한 아웃바운드 포트 인터페이스
 * 
 * 헥사고날 아키텍처에서 이벤트 발행을 위한 포트:
 * 1. 도메인 이벤트를 외부 시스템에 전달하는 역할
 * 2. 도메인 로직과 이벤트 발행 메커니즘의 분리
 * 3. 테스트 용이성 제공
 */
public interface EventPublisherPort {
    
    /**
     * 도메인 이벤트를 발행합니다.
     * 
     * @param event 발행할 도메인 이벤트
     */
    void publishEvent(DomainEvent event);
} 