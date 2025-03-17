package com.example.hexagonalvslayered.hexagonal.adapter.in.rest;

import com.example.hexagonalvslayered.hexagonal.domain.event.TodoEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReceiveCreatedEvent() throws Exception {
        // Given
        String eventJson = """
            {
                "eventId": "%s",
                "eventType": "CREATED",
                "todoId": 1,
                "title": "Test Todo",
                "createdAt": "%s"
            }
            """.formatted(UUID.randomUUID().toString(), LocalDateTime.now());

        // When & Then
        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventJson))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReceiveCompletedEvent() throws Exception {
        // Given
        String eventJson = """
            {
                "eventId": "%s",
                "eventType": "COMPLETED",
                "todoId": 1,
                "title": "Test Todo",
                "createdAt": "%s"
            }
            """.formatted(UUID.randomUUID().toString(), LocalDateTime.now());

        // When & Then
        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventJson))
                .andExpect(status().isOk());
    }
} 