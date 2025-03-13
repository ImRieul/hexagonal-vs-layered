package com.example.hexagonalvslayered.hexagonal.application.port.out;

/**
 * 헥사고날 아키텍처의 아웃바운드 포트 인터페이스
 * 
 * 헥사고날 아키텍처에서 포트 인터페이스의 특징:
 * 1. 도메인 관점에서 정의: 도메인이 필요로 하는 기능을 도메인 용어로 정의
 * 2. 의존성 역전: 도메인이 외부 시스템에 의존하지 않고, 외부 시스템이 도메인에 정의된 인터페이스에 의존
 * 3. 추상화: 외부 시스템의 세부 구현을 추상화하여 도메인 로직과 분리
 * 4. 테스트 용이성: 인터페이스를 통해 외부 시스템을 쉽게 모킹할 수 있어 단위 테스트가 용이
 */
public interface SendNotificationPort {
    
    /**
     * 완료된 Todo에 대한 알림을 전송합니다.
     * 
     * 이 메서드는 도메인 관점에서 필요한 기능을 정의하며,
     * 실제 구현은 어댑터에서 담당합니다.
     * 
     * @param todoId Todo ID
     * @param title Todo 제목
     * @return 알림 전송 성공 여부
     */
    boolean sendCompletionNotification(Long todoId, String title);
} 