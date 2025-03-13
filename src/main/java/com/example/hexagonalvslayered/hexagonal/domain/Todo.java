package com.example.hexagonalvslayered.hexagonal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 헥사고날 아키텍처의 도메인 모델
 * 
 * 헥사고날 아키텍처에서 도메인 모델의 특징:
 * 1. 비즈니스 로직 포함: 도메인 객체가 자체적인 비즈니스 로직을 포함하는 풍부한 객체로 설계됨
 * 2. 캡슐화: 상태 변경 로직이 도메인 객체 내부에 캡슐화됨
 * 3. 외부 의존성 없음: 도메인 모델은 외부 시스템이나 인프라에 의존하지 않음
 * 4. 순수한 자바 객체(POJO): 특정 프레임워크나 기술에 의존하지 않는 순수한 자바 객체로 구현
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {
    
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Todo를 완료 상태로 변경하는 비즈니스 로직
     * 헥사고날 아키텍처에서는 이러한 상태 변경 로직이 도메인 객체 내부에 캡슐화됨
     */
    public void markAsCompleted() {
        this.completed = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Todo의 제목과 설명을 업데이트하는 비즈니스 로직
     * 헥사고날 아키텍처에서는 이러한 상태 변경 로직이 도메인 객체 내부에 캡슐화됨
     */
    public void update(String title, String description) {
        this.title = title;
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }
} 