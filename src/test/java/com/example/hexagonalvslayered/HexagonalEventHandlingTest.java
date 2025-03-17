package com.example.hexagonalvslayered;

import com.example.hexagonalvslayered.hexagonal.application.port.out.EventPublisherPort;
import com.example.hexagonalvslayered.hexagonal.domain.Todo;
import com.example.hexagonalvslayered.hexagonal.domain.event.DomainEvent;
import com.example.hexagonalvslayered.hexagonal.domain.event.TodoCompletedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * 헥사고날 아키텍처의 이벤트 처리 방식을 보여주는 단위 테스트
 * 
 * 이 테스트는 헥사고날 아키텍처의 이벤트 처리 및 외부 시스템 통합 방식을 검증합니다.
 * 헥사고날 아키텍처는 이벤트 기반 통신을 사용하여 느슨한 결합을 유지합니다.
 * 
 * 주요 비교 포인트:
 * 1. 이벤트 발행을 통한 외부 시스템 통합
 * 2. 외부 시스템 장애 시 핵심 비즈니스 로직에 미치는 영향
 * 3. 시스템 간 결합도
 */
@ExtendWith(MockitoExtension.class)
class HexagonalEventHandlingTest {

    @Mock
    private RestTemplate restTemplate;
    
    @Mock
    private EventPublisherPort eventPublisher;

    @Test
    void shouldPublishEventWhenTodoCompleted() {
        // Given
        Todo todo = new Todo(1L, "테스트 할 일", false);
        
        // When
        todo.markAsCompleted();
        eventPublisher.publishEvent(todo.getDomainEvents().get(0));
        
        // Then
        ArgumentCaptor<DomainEvent> eventCaptor = ArgumentCaptor.forClass(DomainEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
        
        DomainEvent capturedEvent = eventCaptor.getValue();
        assertTrue(capturedEvent instanceof TodoCompletedEvent);
        TodoCompletedEvent todoCompletedEvent = (TodoCompletedEvent) capturedEvent;
        assertEquals(1L, todoCompletedEvent.getTodoId());
        assertEquals("테스트 할 일", todoCompletedEvent.getTitle());
    }
} 