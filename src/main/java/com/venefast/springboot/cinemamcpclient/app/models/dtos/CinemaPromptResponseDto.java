package com.venefast.springboot.cinemamcpclient.app.models.dtos;

/**
 * Immutable DTO record representing the cinema assistant's response.
 */
public record CinemaPromptResponseDto(
    String answer,
    String model,
    String timestamp
) {}
