package com.example.hexagonalvslayered.hexagonal.adapter.in.rest;

import com.example.hexagonalvslayered.hexagonal.domain.event.TodoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    @PostMapping
    public ResponseEntity<Void> receiveEvent(@RequestBody TodoEvent event) {
        log.info("Received event via REST API: {}", event);
        
        // 이벤트 타입에 따라 다른 처리 로직을 구현할 수 있습니다.
        switch (TodoEvent.EventType.valueOf(event.getEventType())) {
            case CREATED:
                log.info("Todo created: {}", event.getTitle());
                break;
            case UPDATED:
                log.info("Todo updated: {}", event.getTitle());
                break;
            case DELETED:
                log.info("Todo deleted: {}", event.getTodoId());
                break;
            case COMPLETED:
                log.info("Todo completed: {}", event.getTitle());
                break;
            default:
                log.warn("Unknown event type: {}", event.getEventType());
        }
        
        return ResponseEntity.ok().build();
    }
} 