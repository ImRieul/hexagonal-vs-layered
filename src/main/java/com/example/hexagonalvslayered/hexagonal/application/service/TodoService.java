package com.example.hexagonalvslayered.hexagonal.application.service;

import com.example.hexagonalvslayered.hexagonal.application.port.in.GetTodoQuery;
import com.example.hexagonalvslayered.hexagonal.application.port.in.ManageTodoUseCase;
import com.example.hexagonalvslayered.hexagonal.application.port.out.LoadTodoPort;
import com.example.hexagonalvslayered.hexagonal.application.port.out.SaveTodoPort;
import com.example.hexagonalvslayered.hexagonal.application.port.out.SendNotificationPort;
import com.example.hexagonalvslayered.hexagonal.domain.Todo;
import lombok.RequiredArgsConstructor;
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
 */
@Service
@RequiredArgsConstructor
public class TodoService implements GetTodoQuery, ManageTodoUseCase {
    
    // 헥사고날 아키텍처에서는 서비스가 구체적인 구현체가 아닌 포트 인터페이스에 의존
    // 이를 통해 외부 시스템과의 결합도를 낮춤
    private final LoadTodoPort loadTodoPort;
    private final SaveTodoPort saveTodoPort;
    private final SendNotificationPort sendNotificationPort;
    
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
        
        return saveTodoPort.saveTodo(todo);
    }
    
    @Override
    @Transactional
    public Todo updateTodo(Long id, UpdateTodoCommand command) {
        Todo todo = loadTodoPort.loadTodoById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        
        // 헥사고날 아키텍처에서는 도메인 객체가 비즈니스 로직을 포함하는 풍부한 객체로 설계됨
        // 도메인 로직(업데이트)이 도메인 객체 내부에 캡슐화됨
        todo.update(command.getTitle(), command.getDescription());
        
        return saveTodoPort.saveTodo(todo);
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
        todo.markAsCompleted();
        
        Todo updatedTodo = saveTodoPort.saveTodo(todo);
        
        // 헥사고날 아키텍처에서는 포트 인터페이스를 통해 외부 시스템과 통신
        // 이를 통해 서비스 계층과 외부 시스템 간의 결합도를 낮춤
        // 단위 테스트 시 포트 인터페이스를 모킹하여 외부 시스템과 분리된 테스트 가능
        sendNotificationPort.sendCompletionNotification(updatedTodo.getId(), updatedTodo.getTitle());
        
        return updatedTodo;
    }
} 