package com.example.hexagonalvslayered.hexagonal.adapter.out.notification;

import com.example.hexagonalvslayered.common.ExternalNotificationService;
import com.example.hexagonalvslayered.hexagonal.application.port.out.SendNotificationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 헥사고날 아키텍처의 아웃바운드 어댑터 구현
 * 
 * 헥사고날 아키텍처에서 어댑터의 특징:
 * 1. 포트 인터페이스 구현: 도메인에서 정의한 포트 인터페이스를 구현
 * 2. 외부 시스템 연결: 도메인과 외부 시스템 사이의 변환 역할 담당
 * 3. 인프라 세부사항 캡슐화: 외부 시스템의 세부 구현을 도메인으로부터 숨김
 * 4. 교체 용이성: 외부 시스템이 변경되어도 포트 인터페이스가 유지되면 도메인 로직에 영향 없음
 */
@Component
@RequiredArgsConstructor
public class NotificationAdapter implements SendNotificationPort {
    
    // 실제 외부 시스템과의 통신을 담당하는 서비스
    private final ExternalNotificationService externalNotificationService;
    
    /**
     * 포트 인터페이스에 정의된 메서드를 구현하여 실제 외부 시스템과 통신
     * 이 어댑터는 도메인 로직과 외부 시스템 사이의 번역기 역할을 함
     */
    @Override
    public boolean sendCompletionNotification(Long todoId, String title) {
        // 외부 시스템 호출을 어댑터 내부에 캡슐화하여 도메인 로직과 분리
        return externalNotificationService.sendCompletionNotification(todoId, title);
    }
} 