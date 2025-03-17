package com.example.hexagonalvslayered.hexagonal.adapter.out.rest;

import com.example.hexagonalvslayered.hexagonal.application.port.out.EventPublisherPort;
import com.example.hexagonalvslayered.hexagonal.application.port.out.PublishEventPort;
import com.example.hexagonalvslayered.hexagonal.config.RestTemplateConfig;
import com.example.hexagonalvslayered.hexagonal.domain.event.DomainEvent;
import com.example.hexagonalvslayered.hexagonal.domain.event.TodoCompletedEvent;
import com.example.hexagonalvslayered.hexagonal.domain.event.TodoCreatedEvent;
import com.example.hexagonalvslayered.hexagonal.domain.event.TodoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestApiEventPublisherAdapter implements EventPublisherPort, PublishEventPort {

    private final RestTemplate restTemplate;
    private final RestTemplateConfig restTemplateConfig;

    @Override
    public void publishEvent(DomainEvent event) {
        try {
            TodoEvent todoEvent = convertToTodoEvent(event);
            publishTodoEvent(todoEvent);
        } catch (Exception e) {
            log.error("이벤트 발행 실패: {}", e.getMessage());
        }
    }

    @Override
    public void publishTodoEvent(TodoEvent event) {
        try {
            if (event.getEventId() == null) {
                event.setEventId(UUID.randomUUID().toString());
            }
            
            log.info("Publishing todo event via REST API: {}", event);
            String eventEndpoint = restTemplateConfig.getFullEventUrl();
            ResponseEntity<Void> response = restTemplate.postForEntity(eventEndpoint, event, Void.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Event sent successfully: {}", event);
            } else {
                log.error("Failed to send event: {}, status code: {}", event, response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("이벤트 REST API 발행 실패: {}", e.getMessage());
        }
    }
    
    private TodoEvent convertToTodoEvent(DomainEvent event) {
        if (event instanceof TodoCreatedEvent createdEvent) {
            return TodoEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .eventType(TodoEvent.EventType.CREATED.name())
                    .todoId(createdEvent.getTodoId())
                    .title(createdEvent.getTitle())
                    .createdAt(event.getOccurredAt())
                    .build();
        } else if (event instanceof TodoCompletedEvent completedEvent) {
            return TodoEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .eventType(TodoEvent.EventType.COMPLETED.name())
                    .todoId(completedEvent.getTodoId())
                    .title(completedEvent.getTitle())
                    .createdAt(event.getOccurredAt())
                    .build();
        } else {
            throw new IllegalArgumentException("Unsupported event type: " + event.getClass().getName());
        }
    }
} 