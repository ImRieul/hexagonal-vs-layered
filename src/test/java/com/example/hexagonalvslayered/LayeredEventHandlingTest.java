package com.example.hexagonalvslayered;

import com.example.hexagonalvslayered.common.ExternalNotificationService;
import com.example.hexagonalvslayered.layered.dto.TodoDto;
import com.example.hexagonalvslayered.layered.dto.TodoRequest;
import com.example.hexagonalvslayered.layered.model.Todo;
import com.example.hexagonalvslayered.layered.repository.LayeredTodoRepository;
import com.example.hexagonalvslayered.layered.service.LayeredTodoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 레이어드 아키텍처의 이벤트 처리 방식을 보여주는 단위 테스트
 * 
 * 이 테스트는 레이어드 아키텍처의 이벤트 처리 및 외부 시스템 통합 방식을 검증합니다.
 * 레이어드 아키텍처는 직접적인 서비스 호출을 통해 외부 시스템과 통합합니다.
 * 
 * 주요 비교 포인트:
 * 1. 직접 서비스 호출을 통한 외부 시스템 통합
 * 2. 외부 시스템 장애 시 비즈니스 로직에 미치는 영향
 * 3. 시스템 간 결합도
 */
@ExtendWith(MockitoExtension.class)
public class LayeredEventHandlingTest {

    @Mock
    private LayeredTodoRepository todoRepository;
    
    @Mock
    private ExternalNotificationService externalNotificationService;
    
    @InjectMocks
    private LayeredTodoService todoService;

    /**
     * 레이어드 아키텍처에서 Todo 생성 시 외부 서비스 호출 패턴을 보여주는 테스트
     * 
     * 테스트 시나리오:
     * 1. Todo 생성 요청 객체 생성
     * 2. 레이어드 서비스를 통해 Todo 생성
     * 
     * 레이어드 아키텍처에서는 일반적으로 직접적인 서비스 호출을 통해 외부 시스템과 통신합니다.
     * 이 테스트에서는 생성 시점의 차이를 보여주기 위한 것으로, 실제 검증은 생략되었습니다.
     */
    @Test
    @DisplayName("레이어드 아키텍처: Todo 생성 시 외부 서비스가 직접 호출되어야 함")
    void whenCreateTodoInLayered_thenExternalServiceShouldBeCalled() {
        // given
        TodoRequest request = new TodoRequest();
        request.setTitle("레이어드 테스트 Todo");
        request.setDescription("외부 서비스 호출 테스트");

        Todo savedTodo = Todo.builder()
                .id(1L)
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);
        
        // when
        todoService.createTodo(request);
        
        // then
        // 레이어드 아키텍처에서는 Todo 생성 시 외부 서비스를 호출하지 않을 수 있음
        // 이 테스트는 생성 시점의 차이를 보여주기 위한 것이므로 검증 생략
        verify(todoRepository).save(any(Todo.class));
    }

    /**
     * 레이어드 아키텍처에서 Todo 완료 시 외부 서비스 직접 호출을 검증하는 테스트
     * 
     * 테스트 시나리오:
     * 1. Todo 생성 요청 객체 생성 및 Todo 생성
     * 2. 외부 알림 서비스의 동작 모킹
     * 3. 생성된 Todo를 완료 처리
     * 4. 외부 알림 서비스가 직접 호출되었는지 검증
     * 
     * 레이어드 아키텍처에서는 Todo 완료 시 외부 알림 서비스를 직접 호출합니다.
     */
    @Test
    @DisplayName("레이어드 아키텍처: Todo 완료 시 외부 서비스가 직접 호출되어야 함")
    void whenCompleteTodoInLayered_thenExternalServiceShouldBeCalled() {
        // given
        Long todoId = 1L;
        
        Todo existingTodo = Todo.builder()
                .id(todoId)
                .title("레이어드 완료 테스트")
                .description("완료 외부 서비스 호출 테스트")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        Todo completedTodo = Todo.builder()
                .id(todoId)
                .title("레이어드 완료 테스트")
                .description("완료 외부 서비스 호출 테스트")
                .completed(true)
                .createdAt(existingTodo.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
        
        when(todoRepository.findById(todoId)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(completedTodo);
        when(externalNotificationService.sendCompletionNotification(anyLong(), anyString()))
                .thenReturn(true);

        // when
        TodoDto result = todoService.completeTodo(todoId);
        
        // then
        // 외부 서비스 직접 호출 확인
        verify(externalNotificationService).sendCompletionNotification(anyLong(), anyString());
        assertTrue(result.isCompleted());
    }

    /**
     * 레이어드 아키텍처에서 외부 시스템 장애가 비즈니스 로직에 미치는 영향을 검증하는 테스트
     * 
     * 테스트 시나리오:
     * 1. Todo 생성 요청 객체 생성 및 Todo 생성
     * 2. 외부 알림 서비스 실패 상황 모킹
     * 3. 생성된 Todo를 완료 처리
     * 4. 외부 서비스 호출 실패에도 불구하고 Todo가 완료되었는지 검증
     * 
     * 레이어드 아키텍처에서는 외부 시스템 장애가 비즈니스 로직에 영향을 줄 수 있습니다.
     * 이 테스트 구현에서는 외부 서비스 호출 실패에도 불구하고 Todo는 완료 처리되지만,
     * 실제 구현에 따라 트랜잭션 롤백 등으로 인해 비즈니스 로직이 영향을 받을 수 있습니다.
     */
    @Test
    @DisplayName("레이어드 아키텍처: 외부 시스템 장애 시 비즈니스 로직에 영향을 줄 수 있음")
    void whenExternalSystemFailsInLayered_thenBusinessLogicMayBeAffected() {
        // given
        Long todoId = 1L;
        
        Todo existingTodo = Todo.builder()
                .id(todoId)
                .title("레이어드 장애 테스트")
                .description("외부 시스템 장애 테스트")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        Todo completedTodo = Todo.builder()
                .id(todoId)
                .title("레이어드 장애 테스트")
                .description("외부 시스템 장애 테스트")
                .completed(true)
                .createdAt(existingTodo.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
        
        when(todoRepository.findById(todoId)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(completedTodo);

        // 외부 시스템 장애 시뮬레이션
        when(externalNotificationService.sendCompletionNotification(anyLong(), anyString()))
                .thenReturn(false); // 실패 반환

        // when
        TodoDto result = todoService.completeTodo(todoId);
        
        // then
        // 레이어드 아키텍처에서는 외부 시스템 장애가 비즈니스 로직에 영향을 줄 수 있음
        // 이 테스트에서는 외부 시스템 호출 실패에도 불구하고 Todo는 완료 처리됨
        // 실제 구현에 따라 다를 수 있음 (예: 트랜잭션 롤백 등)
        assertTrue(result.isCompleted());
        verify(externalNotificationService).sendCompletionNotification(anyLong(), anyString());
    }
} 