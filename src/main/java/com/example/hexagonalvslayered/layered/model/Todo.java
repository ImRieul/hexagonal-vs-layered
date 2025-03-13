package com.example.hexagonalvslayered.layered.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 레이어드 아키텍처의 도메인 모델
 * 
 * 레이어드 아키텍처에서 도메인 모델의 특징:
 * 1. 주로 데이터 저장을 위한 구조: 비즈니스 로직보다는 데이터 저장에 초점
 * 2. 외부 의존성 존재: JPA 어노테이션 등 인프라 기술에 의존
 * 3. 단순 데이터 구조체: 주로 getter/setter를 통한 단순 데이터 접근
 * 4. 비즈니스 로직 부재: 비즈니스 로직은 주로 서비스 계층에 위치
 */
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    private String description;
    
    private boolean completed;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // 레이어드 아키텍처에서는 도메인 객체에 비즈니스 로직이 거의 없음
    // 대신 서비스 계층에서 이 객체의 상태를 변경하는 로직을 구현
    // 예: TodoService.completeTodo() 메서드에서 todo.setCompleted(true) 호출
} 