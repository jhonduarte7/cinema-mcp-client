package com.venefast.springboot.cinemamcpclient.app.services;

import com.venefast.springboot.cinemamcpclient.app.models.dtos.CinemaPromptRequestDto;
import com.venefast.springboot.cinemamcpclient.app.models.dtos.CinemaPromptResponseDto;
import com.venefast.springboot.cinemamcpclient.app.models.dtos.CinemaStatusDto;
import com.venefast.springboot.cinemamcpclient.app.services.impl.CinemaAssistantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CinemaAssistantService Unit Tests")
class CinemaAssistantServiceTest {

    @Mock
    private ChatClient chatClient;

    private CinemaAssistantServiceImpl cinemaAssistantService;

    @BeforeEach
    void setUp() {
        cinemaAssistantService = new CinemaAssistantServiceImpl(
            chatClient,
            "test-model",
            "http://localhost:8090",
            true
        );
    }

    @Test
    @DisplayName("askAssistant should return formatted answer when model responds")
    void shouldReturnAnswerWhenSuccessful() {
        ChatClient.ChatClientRequestSpec requestSpec = mock(ChatClient.ChatClientRequestSpec.class);
        ChatClient.CallResponseSpec callResponseSpec = mock(ChatClient.CallResponseSpec.class);

        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.system(any(String.class))).thenReturn(requestSpec);
        when(requestSpec.user(any(Consumer.class))).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(callResponseSpec);
        when(callResponseSpec.content()).thenReturn("Here are the top sci-fi films: Inception, Interstellar.");

        CinemaPromptRequestDto request = new CinemaPromptRequestDto("What are the best sci-fi movies?", "Sci-Fi");
        CinemaPromptResponseDto response = cinemaAssistantService.askAssistant(request);

        assertNotNull(response);
        assertEquals("Here are the top sci-fi films: Inception, Interstellar.", response.answer());
        assertNotNull(response.timestamp());
    }

    @Test
    @DisplayName("askAssistant should handle exceptions gracefully and return diagnostic message")
    void shouldHandleExceptionGracefully() {
        when(chatClient.prompt()).thenThrow(new RuntimeException("Connection refused to Ollama"));

        CinemaPromptRequestDto request = new CinemaPromptRequestDto("Hello");
        CinemaPromptResponseDto response = cinemaAssistantService.askAssistant(request);

        assertNotNull(response);
        assertTrue(response.answer().contains("Unable to complete request"));
        assertTrue(response.answer().contains("Connection refused to Ollama"));
    }

    @Test
    @DisplayName("getStatus should return client status")
    void shouldReturnClientStatus() {
        CinemaStatusDto status = cinemaAssistantService.getStatus();

        assertNotNull(status);
        assertEquals("UP", status.status());
        assertTrue(status.mcpClientEnabled());
    }
}
