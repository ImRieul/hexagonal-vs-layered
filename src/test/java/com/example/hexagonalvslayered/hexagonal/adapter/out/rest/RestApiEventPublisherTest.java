package com.example.hexagonalvslayered.hexagonal.adapter.out.rest;

import com.example.hexagonalvslayered.hexagonal.config.RestTemplateConfig;
import com.example.hexagonalvslayered.hexagonal.domain.event.TodoCompletedEvent;
import com.example.hexagonalvslayered.hexagonal.domain.event.TodoCreatedEvent;
import com.example.hexagonalvslayered.hexagonal.domain.event.TodoEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestApiEventPublisherTest {

    @Mock
    private RestTemplate restTemplate;
    
    @Mock
    private RestTemplateConfig restTemplateConfig;

    private RestApiEventPublisherAdapter eventPublisher;

    @BeforeEach
    void setUp() {
        eventPublisher = new RestApiEventPublisherAdapter(restTemplate, restTemplateConfig);
        when(restTemplateConfig.getFullEventUrl()).thenReturn("http://test-url/api/events");
        when(restTemplate.postForEntity(anyString(), any(), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
    }

    @Test
    void shouldPublishTodoCreatedEvent() {
        // Given
        TodoCreatedEvent createdEvent = new TodoCreatedEvent(1L, "Test Todo");
        
        // When
        eventPublisher.publishEvent(createdEvent);
        
        // Then
        ArgumentCaptor<TodoEvent> eventCaptor = ArgumentCaptor.forClass(TodoEvent.class);
        verify(restTemplate).postForEntity(anyString(), eventCaptor.capture(), eq(Void.class));
        
        TodoEvent capturedEvent = eventCaptor.getValue();
        assertEquals(1L, capturedEvent.getTodoId());
        assertEquals("Test Todo", capturedEvent.getTitle());
        assertEquals(TodoEvent.EventType.CREATED.name(), capturedEvent.getEventType());
    }

    @Test
    void shouldPublishTodoCompletedEvent() {
        // Given
        TodoCompletedEvent completedEvent = new TodoCompletedEvent(1L, "Test Todo");
        
        // When
        eventPublisher.publishEvent(completedEvent);
        
        // Then
        ArgumentCaptor<TodoEvent> eventCaptor = ArgumentCaptor.forClass(TodoEvent.class);
        verify(restTemplate).postForEntity(anyString(), eventCaptor.capture(), eq(Void.class));
        
        TodoEvent capturedEvent = eventCaptor.getValue();
        assertEquals(1L, capturedEvent.getTodoId());
        assertEquals("Test Todo", capturedEvent.getTitle());
        assertEquals(TodoEvent.EventType.COMPLETED.name(), capturedEvent.getEventType());
    }

    @Test
    void shouldPublishTodoEventDirectly() {
        // Given
        TodoEvent event = TodoEvent.builder()
                .todoId(1L)
                .title("Test Todo")
                .eventType(TodoEvent.EventType.CREATED.name())
                .createdAt(LocalDateTime.now())
                .build();
        
        // When
        eventPublisher.publishTodoEvent(event);
        
        // Then
        verify(restTemplate).postForEntity(anyString(), eq(event), eq(Void.class));
    }
} 