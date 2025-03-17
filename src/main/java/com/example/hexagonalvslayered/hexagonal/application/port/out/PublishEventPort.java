package com.example.hexagonalvslayered.hexagonal.application.port.out;

import com.example.hexagonalvslayered.hexagonal.domain.event.TodoEvent;

public interface PublishEventPort {
    void publishTodoEvent(TodoEvent event);
} 