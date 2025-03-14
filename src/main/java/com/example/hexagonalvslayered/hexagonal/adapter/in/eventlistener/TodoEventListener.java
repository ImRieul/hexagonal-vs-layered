package com.example.hexagonalvslayered.hexagonal.adapter.in.eventlistener;

import com.example.hexagonalvslayered.hexagonal.application.port.out.SendNotificationPort;
import com.example.hexagonalvslayered.hexagonal.domain.event.TodoCompletedEvent;
import com.example.hexagonalvslayered.hexagonal.domain.event.TodoCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 도메인 이벤트를 처리하는 이벤트 리스너
 * 
 * 이벤트 기반 통신의 특징:
 * 1. 비동기 처리: 이벤트 발행자와 구독자 간의 비동기 통신
 * 2. 느슨한 결합: 발행자는 구독자에 대한 정보 없이 이벤트만 발행
 * 3. 확장성: 새로운 구독자 추가가 용이함
 * 4. 관심사 분리: 각 구독자는 자신이 관심 있는 이벤트만 처리
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TodoEventListener {
    
    private final SendNotificationPort sendNotificationPort;
    
    /**
     * Todo 완료 이벤트를 비동기적으로 처리합니다.
     */
    @Async
    @EventListener
    public void handleTodoCompletedEvent(TodoCompletedEvent event) {
        log.info("Todo completed event received: {}", event);
        
        // 알림 전송
        sendNotificationPort.sendCompletionNotification(event.getTodoId(), event.getTitle());
    }
    
    /**
     * Todo 생성 이벤트를 비동기적으로 처리합니다.
     */
    @Async
    @EventListener
    public void handleTodoCreatedEvent(TodoCreatedEvent event) {
        log.info("Todo created event received: {}", event);
        
        // 여기에 Todo 생성 시 필요한 추가 작업을 구현할 수 있습니다.
        // 예: 통계 업데이트, 다른 시스템에 알림 등
    }
} 