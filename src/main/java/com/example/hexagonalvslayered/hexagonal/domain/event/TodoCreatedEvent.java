package com.example.hexagonalvslayered.hexagonal.domain.event;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Todo가 생성되었을 때 발생하는 도메인 이벤트
 */
@Getter
public class TodoCreatedEvent implements DomainEvent {
    
    private final Long todoId;
    private final String title;
    private final LocalDateTime occurredAt;
    
    public TodoCreatedEvent(Long todoId, String title) {
        this.todoId = todoId;
        this.title = title;
        this.occurredAt = LocalDateTime.now();
    }
    
    @Override
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
} 