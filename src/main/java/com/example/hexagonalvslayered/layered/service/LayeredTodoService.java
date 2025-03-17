package com.example.hexagonalvslayered.layered.service;

import com.example.hexagonalvslayered.common.ExternalNotificationService;
import com.example.hexagonalvslayered.layered.dto.TodoDto;
import com.example.hexagonalvslayered.layered.dto.TodoRequest;
import com.example.hexagonalvslayered.layered.model.Todo;
import com.example.hexagonalvslayered.layered.repository.LayeredTodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 레이어드 아키텍처의 서비스 계층 구현
 * 
 * 레이어드 아키텍처의 특징:
 * 1. 수직적 계층 구조: Controller → Service → Repository 순으로 의존성이 흐름
 * 2. 직접적인 의존성: 서비스 계층이 Repository와 외부 서비스에 직접 의존
 * 3. 도메인 모델: 주로 데이터 저장을 위한 구조로 설계되며, 비즈니스 로직은 서비스 계층에 위치
 */
@Service
@RequiredArgsConstructor
public class LayeredTodoService {
    
    // 레이어드 아키텍처에서는 서비스가 Repository에 직접 의존
    private final LayeredTodoRepository todoRepository;
    
    // 레이어드 아키텍처에서는 외부 시스템에 직접 의존
    // 이는 서비스 계층과 외부 시스템 간의 강한 결합을 만듦
    private final ExternalNotificationService notificationService;
    
    public List<TodoDto> getAllTodos() {
        return todoRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    public TodoDto getTodoById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        return mapToDto(todo);
    }
    
    @Transactional
    public TodoDto createTodo(TodoRequest request) {
        Todo todo = Todo.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        Todo savedTodo = todoRepository.save(todo);
        return mapToDto(savedTodo);
    }
    
    @Transactional
    public TodoDto updateTodo(Long id, TodoRequest request) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        
        // 레이어드 아키텍처에서는 도메인 모델이 주로 데이터 저장을 위한 구조로 설계됨
        // 비즈니스 로직은 서비스 계층에 위치하는 경향이 있음
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setUpdatedAt(LocalDateTime.now());
        
        Todo updatedTodo = todoRepository.save(todo);
        return mapToDto(updatedTodo);
    }
    
    @Transactional
    public void deleteTodo(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new RuntimeException("Todo not found with id: " + id);
        }
        todoRepository.deleteById(id);
    }
    
    @Transactional
    public TodoDto completeTodo(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        
        // 레이어드 아키텍처에서는 도메인 객체가 단순 데이터 구조체로 사용되는 경향이 있음
        // 비즈니스 로직(완료 처리)이 서비스 계층에 구현됨
        todo.setCompleted(true);
        todo.setUpdatedAt(LocalDateTime.now());
        
        Todo updatedTodo = todoRepository.save(todo);
        
        // 레이어드 아키텍처에서는 외부 시스템(알림 서비스)에 직접 의존
        // 이로 인해 서비스 계층과 외부 시스템 간의 강한 결합이 발생
        // 단위 테스트 시 실제 외부 시스템을 모킹해야 함
        notificationService.sendCompletionNotification(updatedTodo.getId(), updatedTodo.getTitle());
        
        return mapToDto(updatedTodo);
    }
    
    private TodoDto mapToDto(Todo todo) {
        return TodoDto.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .completed(todo.isCompleted())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .build();
    }
} 