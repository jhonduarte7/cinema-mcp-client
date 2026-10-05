package com.venefast.springboot.cinemamcpclient.app.services;

import com.venefast.springboot.cinemamcpclient.app.models.dtos.CinemaPromptRequestDto;
import com.venefast.springboot.cinemamcpclient.app.models.dtos.CinemaPromptResponseDto;
import com.venefast.springboot.cinemamcpclient.app.models.dtos.CinemaStatusDto;

/**
 * Service contract for interacting with the cinema AI assistant.
 */
public interface CinemaAssistantService {

    /**
     * Sends a natural language prompt to the AI assistant, which can invoke Movie MCP Server tools.
     *
     * @param request prompt payload
     * @return AI assistant answer
     */
    CinemaPromptResponseDto askAssistant(CinemaPromptRequestDto request);

    /**
     * Requests tailored movie recommendations by genre.
     *
     * @param genre genre filter query (e.g. Sci-Fi, Drama)
     * @return AI assistant recommendation
     */
    CinemaPromptResponseDto recommendByGenre(String genre);

    /**
     * Checks client readiness, Ollama model configuration, and MCP connection status.
     *
     * @return status DTO
     */
    CinemaStatusDto getStatus();
}
