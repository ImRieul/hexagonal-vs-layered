package com.example.hexagonalvslayered.layered.controller;

import com.example.hexagonalvslayered.layered.dto.TodoDto;
import com.example.hexagonalvslayered.layered.dto.TodoRequest;
import com.example.hexagonalvslayered.layered.service.LayeredTodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/layered/todos")
@RequiredArgsConstructor
public class LayeredTodoController {
    
    private final LayeredTodoService todoService;
    
    @GetMapping
    public ResponseEntity<List<TodoDto>> getAllTodos() {
        return ResponseEntity.ok(todoService.getAllTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TodoDto> getTodoById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.getTodoById(id));
    }
    
    @PostMapping
    public ResponseEntity<TodoDto> createTodo(@RequestBody TodoRequest request) {
        return new ResponseEntity<>(todoService.createTodo(request), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TodoDto> updateTodo(@PathVariable Long id, @RequestBody TodoRequest request) {
        return ResponseEntity.ok(todoService.updateTodo(id, request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/complete")
    public ResponseEntity<TodoDto> completeTodo(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.completeTodo(id));
    }
} 