package com.example.hexagonalvslayered.hexagonal.adapter.in.web;

import com.example.hexagonalvslayered.hexagonal.application.port.in.GetTodoQuery;
import com.example.hexagonalvslayered.hexagonal.application.port.in.ManageTodoUseCase;
import com.example.hexagonalvslayered.hexagonal.domain.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hexagonal/todos")
@RequiredArgsConstructor
public class HexagonalTodoController {
    
    private final GetTodoQuery getTodoQuery;
    private final ManageTodoUseCase manageTodoUseCase;
    
    @GetMapping
    public ResponseEntity<List<TodoDto>> getAllTodos() {
        List<Todo> todos = getTodoQuery.getAllTodos();
        List<TodoDto> todoDtos = todos.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(todoDtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TodoDto> getTodoById(@PathVariable Long id) {
        return getTodoQuery.getTodoById(id)
                .map(todo -> ResponseEntity.ok(mapToDto(todo)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<TodoDto> createTodo(@RequestBody TodoRequest request) {
        ManageTodoUseCase.CreateTodoCommand command = 
                new ManageTodoUseCase.CreateTodoCommand(request.getTitle(), request.getDescription());
        Todo todo = manageTodoUseCase.createTodo(command);
        return new ResponseEntity<>(mapToDto(todo), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TodoDto> updateTodo(@PathVariable Long id, @RequestBody TodoRequest request) {
        ManageTodoUseCase.UpdateTodoCommand command = 
                new ManageTodoUseCase.UpdateTodoCommand(request.getTitle(), request.getDescription());
        Todo todo = manageTodoUseCase.updateTodo(id, command);
        return ResponseEntity.ok(mapToDto(todo));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        manageTodoUseCase.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/complete")
    public ResponseEntity<TodoDto> completeTodo(@PathVariable Long id) {
        Todo todo = manageTodoUseCase.completeTodo(id);
        return ResponseEntity.ok(mapToDto(todo));
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