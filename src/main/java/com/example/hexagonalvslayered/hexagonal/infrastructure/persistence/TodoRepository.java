package com.example.hexagonalvslayered.hexagonal.infrastructure.persistence;

import com.example.hexagonalvslayered.hexagonal.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
} 