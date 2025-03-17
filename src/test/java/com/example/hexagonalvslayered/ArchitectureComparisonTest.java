package com.example.hexagonalvslayered;

import com.example.hexagonalvslayered.common.ExternalNotificationService;
import com.example.hexagonalvslayered.hexagonal.application.port.in.ManageTodoUseCase;
import com.example.hexagonalvslayered.hexagonal.application.port.out.EventPublisherPort;
import com.example.hexagonalvslayered.hexagonal.application.port.out.LoadTodoPort;
import com.example.hexagonalvslayered.hexagonal.application.port.out.SaveTodoPort;
import com.example.hexagonalvslayered.hexagonal.application.port.out.SendNotificationPort;
import com.example.hexagonalvslayered.hexagonal.application.service.HexagonalTodoService;
import com.example.hexagonalvslayered.layered.dto.TodoRequest;
import com.example.hexagonalvslayered.layered.repository.LayeredTodoRepository;
import com.example.hexagonalvslayered.layered.service.LayeredTodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 헥사고날 아키텍처와 레이어드 아키텍처의 차이점을 보여주는 테스트 클래스입니다.
 * 특히 외부 시스템 통합 방식의 차이점에 초점을 맞춥니다.
 * 
 * 두 아키텍처의 주요 차이점:
 * 
 * 1. 의존성 구조
 *    - 레이어드: 서비스가 외부 시스템(Repository, ExternalNotificationService)에 직접 의존
 *    - 헥사고날: 서비스가 포트 인터페이스(LoadTodoPort, SaveTodoPort, SendNotificationPort)에 의존
 * 
 * 2. 도메인 모델
 *    - 레이어드: 도메인 모델이 주로 데이터 저장을 위한 구조로 설계됨, 비즈니스 로직은 서비스 계층에 위치
 *    - 헥사고날: 도메인 모델이 비즈니스 로직을 포함하는 풍부한 객체로 설계됨
 * 
 * 3. 외부 시스템 통합
 *    - 레이어드: 서비스가 외부 시스템을 직접 호출, 강한 결합
 *    - 헥사고날: 서비스가 포트 인터페이스를 통해 외부 시스템과 통신, 약한 결합
 * 
 * 4. 테스트 용이성
 *    - 레이어드: 외부 시스템에 직접 의존하므로 단위 테스트가 어려울 수 있음
 *    - 헥사고날: 포트 인터페이스를 통해 외부 시스템과 분리되어 있어 단위 테스트가 용이함
 * 
 * 5. 통신 방식
 *    - 레이어드: 주로 직접 호출 방식 사용
 *    - 헥사고날: 이벤트 기반 통신을 통한 느슨한 결합 구현
 */
@ExtendWith(MockitoExtension.class)
public class ArchitectureComparisonTest {

    // 헥사고날 아키텍처 관련 목(mock) 객체
    @Mock
    private LoadTodoPort loadTodoPort;
    
    @Mock
    private SaveTodoPort saveTodoPort;
    
    @Mock
    private SendNotificationPort sendNotificationPort;
    
    @Mock
    private EventPublisherPort eventPublisherPort;
    
    // 레이어드 아키텍처 관련 목(mock) 객체
    @Mock
    private LayeredTodoRepository todoRepository;
    
    @Mock
    private ExternalNotificationService externalNotificationService;
    
    // 테스트 대상 서비스
    private HexagonalTodoService hexagonalTodoService;
    private LayeredTodoService layeredTodoService;
    
    @BeforeEach
    void setUp() {
        // 헥사고날 아키텍처 서비스 초기화
        // 포트 인터페이스에 의존하는 구조 (의존성 역전 원칙)
        // 이벤트 기반 통신을 위한 EventPublisherPort 추가
        hexagonalTodoService = new HexagonalTodoService(
                loadTodoPort, saveTodoPort, eventPublisherPort);
        
        // 레이어드 아키텍처 서비스 초기화
        // 구체적인 구현체에 직접 의존하는 구조
        layeredTodoService = new LayeredTodoService(
                todoRepository, externalNotificationService);
    }
    
    /**
     * 헥사고날 아키텍처에서 Todo 객체를 생성하는 헬퍼 메서드
     */
    private com.example.hexagonalvslayered.hexagonal.domain.Todo createHexagonalTodo(
            Long id, String title, String description, boolean completed) {
        return com.example.hexagonalvslayered.hexagonal.domain.Todo.builder()
                .id(id)
                .title(title)
                .description(description)
                .completed(completed)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    
    /**
     * 레이어드 아키텍처에서 Todo 객체를 생성하는 헬퍼 메서드
     */
    private com.example.hexagonalvslayered.layered.model.Todo createLayeredTodo(
            Long id, String title, String description, boolean completed) {
        return com.example.hexagonalvslayered.layered.model.Todo.builder()
                .id(id)
                .title(title)
                .description(description)
                .completed(completed)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    
    /**
     * TodoRequest 객체를 생성하는 헬퍼 메서드
     */
    private TodoRequest createTodoRequest(String title, String description) {
        TodoRequest request = new TodoRequest();
        request.setTitle(title);
        request.setDescription(description);
        return request;
    }
    
    @Test
    @DisplayName("헥사고날 아키텍처: Todo 완료 시 이벤트가 발행되어야 함")
    void givenValidTodo_whenCompleteTodoInHexagonal_thenEventPublished() {
        // Given
        Long todoId = 1L;
        String todoTitle = "테스트 Todo";
        String todoDescription = "헥사고날 아키텍처 테스트";
        
        var todo = createHexagonalTodo(todoId, todoTitle, todoDescription, false);
        var completedTodo = createHexagonalTodo(todoId, todoTitle, todoDescription, true);
        
        when(loadTodoPort.loadTodoById(todoId)).thenReturn(Optional.of(todo));
        when(saveTodoPort.saveTodo(any(com.example.hexagonalvslayered.hexagonal.domain.Todo.class)))
                .thenReturn(completedTodo);
        
        // When
        var result = hexagonalTodoService.completeTodo(todoId);
        
        // Then
        assertTrue(result.isCompleted());
        verify(loadTodoPort, times(1)).loadTodoById(todoId);
        verify(saveTodoPort, times(1))
                .saveTodo(any(com.example.hexagonalvslayered.hexagonal.domain.Todo.class));
        verify(eventPublisherPort, times(1)).publishEvent(any());
    }
    
    @Test
    @DisplayName("레이어드 아키텍처: Todo 완료 시 알림이 정상적으로 전송되어야 함")
    void givenValidTodo_whenCompleteTodoInLayered_thenNotificationSent() {
        // Given
        Long todoId = 1L;
        String todoTitle = "테스트 Todo";
        String todoDescription = "레이어드 아키텍처 테스트";
        
        var todo = createLayeredTodo(todoId, todoTitle, todoDescription, false);
        var completedTodo = createLayeredTodo(todoId, todoTitle, todoDescription, true);
        
        // 레이어드 아키텍처에서는 구체적인 구현체를 직접 모킹
        // 서비스 계층이 외부 시스템에 직접 의존하는 구조
        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(com.example.hexagonalvslayered.layered.model.Todo.class)))
                .thenReturn(completedTodo);
        when(externalNotificationService.sendCompletionNotification(todoId, todoTitle))
                .thenReturn(true);
        
        // When
        var result = layeredTodoService.completeTodo(todoId);
        
        // Then
        assertTrue(result.isCompleted());
        verify(todoRepository, times(1)).findById(todoId);
        verify(todoRepository, times(1))
                .save(any(com.example.hexagonalvslayered.layered.model.Todo.class));
        verify(externalNotificationService, times(1))
                .sendCompletionNotification(todoId, todoTitle);
    }
    
    @Test
    @DisplayName("헥사고날 아키텍처: 외부 시스템 장애 시 예외가 발생하지 않아야 함")
    void givenExternalSystemFailure_whenCompleteTodoInHexagonal_thenNoExceptionThrown() {
        // Given
        Long todoId = 1L;
        String todoTitle = "테스트 Todo";
        String todoDescription = "헥사고날 아키텍처 테스트";
        
        var todo = createHexagonalTodo(todoId, todoTitle, todoDescription, false);
        var completedTodo = createHexagonalTodo(todoId, todoTitle, todoDescription, true);
        
        when(loadTodoPort.loadTodoById(todoId)).thenReturn(Optional.of(todo));
        when(saveTodoPort.saveTodo(any(com.example.hexagonalvslayered.hexagonal.domain.Todo.class)))
                .thenReturn(completedTodo);
        
        // 외부 시스템(이벤트 발행) 장애 시뮬레이션
        doThrow(new RuntimeException("외부 시스템 장애")).when(eventPublisherPort).publishEvent(any());
        
        // When & Then
        // 이벤트 기반 통신에서는 이벤트 발행 실패가 비즈니스 로직 실행에 영향을 주지 않음
        var result = hexagonalTodoService.completeTodo(todoId);
        
        assertTrue(result.isCompleted());
        verify(loadTodoPort, times(1)).loadTodoById(todoId);
        verify(saveTodoPort, times(1))
                .saveTodo(any(com.example.hexagonalvslayered.hexagonal.domain.Todo.class));
        verify(eventPublisherPort, times(1)).publishEvent(any());
    }
    
    @Test
    @DisplayName("레이어드 아키텍처: 외부 시스템 장애 시 예외가 발생해야 함")
    void givenExternalSystemFailure_whenCompleteTodoInLayered_thenExceptionThrown() {
        // Given
        Long todoId = 1L;
        String todoTitle = "테스트 Todo";
        String todoDescription = "레이어드 아키텍처 테스트";
        
        var todo = createLayeredTodo(todoId, todoTitle, todoDescription, false);
        var completedTodo = createLayeredTodo(todoId, todoTitle, todoDescription, true);
        
        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(com.example.hexagonalvslayered.layered.model.Todo.class)))
                .thenReturn(completedTodo);
        
        // 외부 시스템 장애 시뮬레이션
        // 레이어드 아키텍처에서는 외부 시스템을 직접 모킹하여 장애를 시뮬레이션
        when(externalNotificationService.sendCompletionNotification(anyLong(), anyString()))
                .thenThrow(new RuntimeException("외부 알림 서비스 장애"));
        
        // When & Then
        // 레이어드 아키텍처에서는 외부 시스템 장애가 비즈니스 로직에 직접 영향을 줌
        assertThrows(RuntimeException.class, () -> {
            layeredTodoService.completeTodo(todoId);
        });
    }
    
    @Test
    @DisplayName("헥사고날 아키텍처: 도메인 로직이 독립적으로 실행되어야 함")
    void givenTodo_whenMarkAsCompleted_thenTodoShouldBeCompletedAndEventRegistered() {
        // Given
        Long todoId = 1L;
        String todoTitle = "테스트 Todo";
        String todoDescription = "도메인 로직 테스트";
        
        var todo = createHexagonalTodo(todoId, todoTitle, todoDescription, false);
        
        // When
        todo.markAsCompleted();
        
        // Then
        assertTrue(todo.isCompleted());
        assertEquals(LocalDateTime.now().getDayOfYear(), todo.getUpdatedAt().getDayOfYear());
    }
    
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    @DisplayName("헥사고날 아키텍처: 다양한 Todo ID에 대해 포트 인터페이스를 통한 서비스 호출이 정상 작동해야 함")
    void givenDifferentTodoIds_whenCreateTodoInHexagonal_thenShouldSaveSuccessfully(Long todoId) {
        // Given
        String todoTitle = "테스트 Todo " + todoId;
        String todoDescription = "헥사고날 아키텍처 테스트 " + todoId;
        
        ManageTodoUseCase.CreateTodoCommand command = 
                new ManageTodoUseCase.CreateTodoCommand(todoTitle, todoDescription);
        
        var todo = createHexagonalTodo(todoId, todoTitle, todoDescription, false);
        
        when(saveTodoPort.saveTodo(any(com.example.hexagonalvslayered.hexagonal.domain.Todo.class)))
                .thenAnswer(invocation -> {
                    var savedTodo = invocation.getArgument(0);
                    ((com.example.hexagonalvslayered.hexagonal.domain.Todo) savedTodo).setId(todoId);
                    return savedTodo;
                });
        
        // When
        var result = hexagonalTodoService.createTodo(command);
        
        // Then
        assertEquals(todoId, result.getId());
        assertEquals(todoTitle, result.getTitle());
        assertEquals(todoDescription, result.getDescription());
        verify(saveTodoPort, times(1))
                .saveTodo(any(com.example.hexagonalvslayered.hexagonal.domain.Todo.class));
    }
    
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    @DisplayName("레이어드 아키텍처: 다양한 Todo ID에 대해 서비스 계층 호출이 정상 작동해야 함")
    void givenDifferentTodoIds_whenCreateTodoInLayered_thenShouldSaveSuccessfully(Long todoId) {
        // Given
        String todoTitle = "테스트 Todo " + todoId;
        String todoDescription = "레이어드 아키텍처 테스트 " + todoId;
        
        TodoRequest request = createTodoRequest(todoTitle, todoDescription);
        
        var todo = createLayeredTodo(todoId, todoTitle, todoDescription, false);
        
        when(todoRepository.save(any(com.example.hexagonalvslayered.layered.model.Todo.class)))
                .thenAnswer(invocation -> {
                    var savedTodo = invocation.getArgument(0);
                    ((com.example.hexagonalvslayered.layered.model.Todo) savedTodo).setId(todoId);
                    return savedTodo;
                });
        
        // When
        var result = layeredTodoService.createTodo(request);
        
        // Then
        assertEquals(todoId, result.getId());
        assertEquals(todoTitle, result.getTitle());
        assertEquals(todoDescription, result.getDescription());
        verify(todoRepository, times(1))
                .save(any(com.example.hexagonalvslayered.layered.model.Todo.class));
    }
} 