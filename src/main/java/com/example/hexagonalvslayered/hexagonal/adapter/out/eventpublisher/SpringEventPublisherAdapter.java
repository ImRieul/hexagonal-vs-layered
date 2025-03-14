package com.example.hexagonalvslayered.hexagonal.adapter.out.eventpublisher;

import com.example.hexagonalvslayered.hexagonal.application.port.out.EventPublisherPort;
import com.example.hexagonalvslayered.hexagonal.domain.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Spring의 ApplicationEventPublisher를 사용하는 이벤트 퍼블리셔 어댑터
 * 
 * 헥사고날 아키텍처에서 어댑터의 역할:
 * 1. 포트 인터페이스 구현: 도메인에서 정의한 포트 인터페이스를 구현
 * 2. 기술적 세부사항 처리: 특정 기술(여기서는 Spring의 이벤트 시스템)에 대한 세부사항 처리
 * 3. 도메인 로직과 인프라 로직의 분리: 도메인 로직은 포트 인터페이스에 의존하고, 인프라 로직은 어댑터에 구현
 */
@Component
@RequiredArgsConstructor
public class SpringEventPublisherAdapter implements EventPublisherPort {
    
    private final ApplicationEventPublisher applicationEventPublisher;
    
    @Override
    public void publishEvent(DomainEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
} 