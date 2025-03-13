package com.example.hexagonalvslayered.hexagonal.adapter.out.persistence;

import com.example.hexagonalvslayered.hexagonal.application.port.out.LoadTodoPort;
import com.example.hexagonalvslayered.hexagonal.application.port.out.SaveTodoPort;
import com.example.hexagonalvslayered.hexagonal.domain.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TodoPersistenceAdapter implements LoadTodoPort, SaveTodoPort {
    
    private final TodoRepository todoRepository;
    
    @Override
    public List<Todo> loadAllTodos() {
        return todoRepository.findAll().stream()
                .map(this::mapToDomainEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Todo> loadTodoById(Long id) {
        return todoRepository.findById(id)
                .map(this::mapToDomainEntity);
    }
    
    @Override
    public Todo saveTodo(Todo todo) {
        TodoJpaEntity todoJpaEntity = mapToJpaEntity(todo);
        TodoJpaEntity savedEntity = todoRepository.save(todoJpaEntity);
        return mapToDomainEntity(savedEntity);
    }
    
    @Override
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }
    
    private Todo mapToDomainEntity(TodoJpaEntity todoJpaEntity) {
        return Todo.builder()
                .id(todoJpaEntity.getId())
                .title(todoJpaEntity.getTitle())
                .description(todoJpaEntity.getDescription())
                .completed(todoJpaEntity.isCompleted())
                .createdAt(todoJpaEntity.getCreatedAt())
                .updatedAt(todoJpaEntity.getUpdatedAt())
                .build();
    }
    
    private TodoJpaEntity mapToJpaEntity(Todo todo) {
        return TodoJpaEntity.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .completed(todo.isCompleted())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .build();
    }
} 