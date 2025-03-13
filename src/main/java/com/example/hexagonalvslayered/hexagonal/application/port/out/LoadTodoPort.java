package com.example.hexagonalvslayered.hexagonal.application.port.out;

import com.example.hexagonalvslayered.hexagonal.domain.Todo;

import java.util.List;
import java.util.Optional;

public interface LoadTodoPort {
    
    List<Todo> loadAllTodos();
    
    Optional<Todo> loadTodoById(Long id);
} 