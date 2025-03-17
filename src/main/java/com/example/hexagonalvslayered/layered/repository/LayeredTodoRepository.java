package com.example.hexagonalvslayered.layered.repository;

import com.example.hexagonalvslayered.layered.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LayeredTodoRepository extends JpaRepository<Todo, Long> {
} 