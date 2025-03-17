package com.example.hexagonalvslayered.hexagonal.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HexagonalTodoRepository extends JpaRepository<TodoJpaEntity, Long> {
} 