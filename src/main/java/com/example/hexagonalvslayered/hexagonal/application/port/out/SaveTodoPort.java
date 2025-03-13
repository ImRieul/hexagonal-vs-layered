package com.example.hexagonalvslayered.hexagonal.application.port.out;

import com.example.hexagonalvslayered.hexagonal.domain.Todo;

public interface SaveTodoPort {
    
    Todo saveTodo(Todo todo);
    
    void deleteTodo(Long id);
} 