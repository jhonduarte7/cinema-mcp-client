package com.venefast.springboot.cinemamcpclient.app.controllers;

import com.venefast.springboot.cinemamcpclient.app.models.dtos.CinemaPromptRequestDto;
import com.venefast.springboot.cinemamcpclient.app.models.dtos.CinemaPromptResponseDto;
import com.venefast.springboot.cinemamcpclient.app.models.dtos.CinemaStatusDto;
import com.venefast.springboot.cinemamcpclient.app.services.CinemaAssistantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CinemaAssistantController.class)
@DisplayName("CinemaAssistantController REST API Tests")
class CinemaAssistantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CinemaAssistantService cinemaAssistantService;

    @Test
    @DisplayName("POST /api/cinema/ask should return AI assistant answer")
    void shouldAskAssistantSuccessfully() throws Exception {
        CinemaPromptResponseDto responseDto = new CinemaPromptResponseDto(
            "I recommend watching Inception (2010), rating: 8.8/10.",
            "llama3.2",
            "2026-10-05T17:00:00Z"
        );
        when(cinemaAssistantService.askAssistant(any(CinemaPromptRequestDto.class))).thenReturn(responseDto);

        String jsonPayload = """
            {
                "prompt": "Recommend a good sci-fi movie",
                "userPreference": "mind-bending"
            }
            """;

        mockMvc.perform(post("/api/cinema/ask")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.answer").value("I recommend watching Inception (2010), rating: 8.8/10."))
            .andExpect(jsonPath("$.model").value("llama3.2"));
    }

    @Test
    @DisplayName("POST /api/cinema/ask should return 400 when prompt is blank")
    void shouldRejectBlankPrompt() throws Exception {
        String invalidPayload = """
            {
                "prompt": "",
                "userPreference": "anything"
            }
            """;

        mockMvc.perform(post("/api/cinema/ask")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/cinema/recommend should return genre recommendations")
    void shouldRecommendByGenre() throws Exception {
        CinemaPromptResponseDto responseDto = new CinemaPromptResponseDto(
            "Top Sci-Fi recommendations: Interstellar, Blade Runner 2049.",
            "llama3.2",
            "2026-10-05T17:00:00Z"
        );
        when(cinemaAssistantService.recommendByGenre("Sci-Fi")).thenReturn(responseDto);

        mockMvc.perform(get("/api/cinema/recommend").param("genre", "Sci-Fi"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.answer").value("Top Sci-Fi recommendations: Interstellar, Blade Runner 2049."))
            .andExpect(jsonPath("$.model").value("llama3.2"));
    }

    @Test
    @DisplayName("GET /api/cinema/status should return client readiness status")
    void shouldReturnStatus() throws Exception {
        CinemaStatusDto statusDto = new CinemaStatusDto("UP", "llama3.2", "http://localhost:8090", true);
        when(cinemaAssistantService.getStatus()).thenReturn(statusDto);

        mockMvc.perform(get("/api/cinema/status"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"))
            .andExpect(jsonPath("$.ollamaModel").value("llama3.2"))
            .andExpect(jsonPath("$.mcpServerUrl").value("http://localhost:8090"))
            .andExpect(jsonPath("$.mcpClientEnabled").value(true));
    }
}
