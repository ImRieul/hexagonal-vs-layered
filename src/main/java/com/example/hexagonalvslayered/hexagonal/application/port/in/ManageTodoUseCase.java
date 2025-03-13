package com.example.hexagonalvslayered.hexagonal.application.port.in;

import com.example.hexagonalvslayered.hexagonal.domain.Todo;

public interface ManageTodoUseCase {
    
    Todo createTodo(CreateTodoCommand command);
    
    Todo updateTodo(Long id, UpdateTodoCommand command);
    
    void deleteTodo(Long id);
    
    Todo completeTodo(Long id);
    
    class CreateTodoCommand {
        private final String title;
        private final String description;
        
        public CreateTodoCommand(String title, String description) {
            this.title = title;
            this.description = description;
        }
        
        public String getTitle() {
            return title;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    class UpdateTodoCommand {
        private final String title;
        private final String description;
        
        public UpdateTodoCommand(String title, String description) {
            this.title = title;
            this.description = description;
        }
        
        public String getTitle() {
            return title;
        }
        
        public String getDescription() {
            return description;
        }
    }
} 