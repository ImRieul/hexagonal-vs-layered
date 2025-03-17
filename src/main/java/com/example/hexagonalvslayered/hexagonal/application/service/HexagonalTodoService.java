package com.example.hexagonalvslayered.hexagonal.application.service;

import com.example.hexagonalvslayered.hexagonal.application.port.in.GetTodoQuery;
import com.example.hexagonalvslayered.hexagonal.application.port.in.ManageTodoUseCase;
import com.example.hexagonalvslayered.hexagonal.application.port.out.EventPublisherPort;
import com.example.hexagonalvslayered.hexagonal.application.port.out.LoadTodoPort;
import com.example.hexagonalvslayered.hexagonal.application.port.out.SaveTodoPort;
import com.example.hexagonalvslayered.hexagonal.domain.Todo;
import com.example.hexagonalvslayered.hexagonal.domain.event.TodoCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 헥사고날 아키텍처의 서비스 계층 구현
 * 
 * 헥사고날 아키텍처의 특징:
 * 1. 포트와 어댑터 패턴: 도메인 로직이 외부 시스템과 분리됨
 * 2. 의존성 역전 원칙(DIP): 도메인이 인터페이스(포트)에 의존하고, 구현체(어댑터)는 이 인터페이스를 구현
 * 3. 도메인 중심 설계: 도메인 모델이 비즈니스 로직을 포함하는 풍부한 객체로 설계됨
 * 4. 외부 시스템과의 결합도 감소: 포트 인터페이스를 통해 외부 시스템과 통신하므로 결합도가 낮음
 * 5. 이벤트 기반 통신: 도메인 이벤트를 통한 비동기 통신으로 서비스 간 결합도 감소
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HexagonalTodoService implements GetTodoQuery, ManageTodoUseCase {
    
    // 헥사고날 아키텍처에서는 서비스가 구체적인 구현체가 아닌 포트 인터페이스에 의존
    // 이를 통해 외부 시스템과의 결합도를 낮춤
    private final LoadTodoPort loadTodoPort;
    private final SaveTodoPort saveTodoPort;
    private final EventPublisherPort eventPublisherPort;
    
    @Override
    public List<Todo> getAllTodos() {
        return loadTodoPort.loadAllTodos();
    }
    
    @Override
    public Optional<Todo> getTodoById(Long id) {
        return loadTodoPort.loadTodoById(id);
    }
    
    @Override
    @Transactional
    public Todo createTodo(CreateTodoCommand command) {
        Todo todo = Todo.builder()
                .title(command.getTitle())
                .description(command.getDescription())
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        // Todo 저장
        Todo savedTodo = saveTodoPort.saveTodo(todo);
        
        // 도메인 이벤트 등록
        TodoCreatedEvent event = new TodoCreatedEvent(savedTodo.getId(), savedTodo.getTitle());
        savedTodo.registerEvent(event);
        
        // 이벤트 발행
        publishEvents(savedTodo);
        
        return savedTodo;
    }
    
    @Override
    @Transactional
    public Todo updateTodo(Long id, UpdateTodoCommand command) {
        Todo todo = loadTodoPort.loadTodoById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        
        // 헥사고날 아키텍처에서는 도메인 객체가 비즈니스 로직을 포함하는 풍부한 객체로 설계됨
        // 도메인 로직(업데이트)이 도메인 객체 내부에 캡슐화됨
        todo.update(command.getTitle(), command.getDescription());
        
        Todo updatedTodo = saveTodoPort.saveTodo(todo);
        publishEvents(todo);
        
        return updatedTodo;
    }
    
    @Override
    @Transactional
    public void deleteTodo(Long id) {
        if (!loadTodoPort.loadTodoById(id).isPresent()) {
            throw new RuntimeException("Todo not found with id: " + id);
        }
        saveTodoPort.deleteTodo(id);
    }
    
    @Override
    @Transactional
    public Todo completeTodo(Long id) {
        Todo todo = loadTodoPort.loadTodoById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        
        // 헥사고날 아키텍처에서는 도메인 객체가 비즈니스 로직을 포함하는 풍부한 객체로 설계됨
        // 도메인 로직(완료 처리)이 도메인 객체 내부에 캡슐화됨
        // 도메인 객체 내부에서 이벤트 발생
        todo.markAsCompleted();
        
        Todo updatedTodo = saveTodoPort.saveTodo(todo);
        
        // 도메인 이벤트 발행
        publishEvents(todo);
        
        return updatedTodo;
    }
    
    /**
     * 도메인 객체에서 발생한 이벤트를 발행합니다.
     * 이벤트 발행 실패는 핵심 비즈니스 로직에 영향을 주지 않습니다.
     */
    private void publishEvents(Todo todo) {
        todo.pullDomainEvents().forEach(event -> {
            try {
                eventPublisherPort.publishEvent(event);
            } catch (Exception e) {
                log.error("이벤트 발행 실패: {}", e.getMessage());
            }
        });
    }
} 