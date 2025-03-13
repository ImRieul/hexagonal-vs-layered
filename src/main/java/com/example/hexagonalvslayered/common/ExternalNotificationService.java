package com.example.hexagonalvslayered.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 외부 알림 서비스를 시뮬레이션하는 클래스입니다.
 * 실제 프로덕션 환경에서는 외부 API를 호출하는 코드가 들어갈 것입니다.
 */
@Service
@Slf4j
public class ExternalNotificationService {
    
    /**
     * 완료된 Todo에 대한 알림을 전송합니다.
     * 
     * @param todoId Todo ID
     * @param title Todo 제목
     * @return 알림 전송 성공 여부
     */
    public boolean sendCompletionNotification(Long todoId, String title) {
        // 실제로는 외부 API를 호출하는 코드가 들어갈 것입니다.
        log.info("외부 알림 서비스 호출: Todo ID '{}', 제목 '{}' 완료됨", todoId, title);
        
        // 외부 API 호출 시뮬레이션 (지연 시간 추가)
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return true;
    }
} 