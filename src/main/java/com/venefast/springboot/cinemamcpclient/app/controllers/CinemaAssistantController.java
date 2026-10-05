package com.venefast.springboot.cinemamcpclient.app.controllers;

import com.venefast.springboot.cinemamcpclient.app.models.dtos.CinemaPromptRequestDto;
import com.venefast.springboot.cinemamcpclient.app.models.dtos.CinemaPromptResponseDto;
import com.venefast.springboot.cinemamcpclient.app.models.dtos.CinemaStatusDto;
import com.venefast.springboot.cinemamcpclient.app.services.CinemaAssistantService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller exposing cinema AI assistant endpoints.
 */
@RestController
@RequestMapping("/api/cinema")
public class CinemaAssistantController {

    private final CinemaAssistantService cinemaAssistantService;

    public CinemaAssistantController(CinemaAssistantService cinemaAssistantService) {
        this.cinemaAssistantService = cinemaAssistantService;
    }

    /**
     * Sends a natural language query to the Cinema AI assistant.
     *
     * @param request prompt payload
     * @return 200 OK with AI response
     */
    @PostMapping("/ask")
    public ResponseEntity<CinemaPromptResponseDto> askAssistant(@Valid @RequestBody CinemaPromptRequestDto request) {
        CinemaPromptResponseDto response = cinemaAssistantService.askAssistant(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets movie recommendations by genre.
     *
     * @param genre genre filter query
     * @return 200 OK with recommendation
     */
    @GetMapping("/recommend")
    public ResponseEntity<CinemaPromptResponseDto> recommendByGenre(@RequestParam(required = false) String genre) {
        CinemaPromptResponseDto response = cinemaAssistantService.recommendByGenre(genre);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves client configuration and status.
     *
     * @return 200 OK with status
     */
    @GetMapping("/status")
    public ResponseEntity<CinemaStatusDto> getStatus() {
        return ResponseEntity.ok(cinemaAssistantService.getStatus());
    }
}
