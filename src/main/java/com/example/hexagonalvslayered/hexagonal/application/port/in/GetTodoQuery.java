package com.example.hexagonalvslayered.hexagonal.application.port.in;

import com.example.hexagonalvslayered.hexagonal.domain.Todo;

import java.util.List;
import java.util.Optional;

public interface GetTodoQuery {
    
    List<Todo> getAllTodos();
    
    Optional<Todo> getTodoById(Long id);
} 