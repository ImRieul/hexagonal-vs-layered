package com.example.hexagonalvslayered.hexagonal.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoEvent implements DomainEvent {
    
    private String eventId;
    private String eventType;
    private Long todoId;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public enum EventType {
        CREATED,
        UPDATED,
        DELETED,
        COMPLETED
    }
    
    @Override
    public LocalDateTime getOccurredAt() {
        return createdAt;
    }
    
    @Override
    public String toString() {
        return String.format(
            "{\"eventId\":\"%s\",\"eventType\":\"%s\",\"todoId\":%d,\"title\":\"%s\",\"createdAt\":\"%s\"}",
            eventId,
            eventType,
            todoId,
            title,
            createdAt
        );
    }
} 