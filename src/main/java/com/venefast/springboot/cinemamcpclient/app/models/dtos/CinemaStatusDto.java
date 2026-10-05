package com.venefast.springboot.cinemamcpclient.app.models.dtos;

/**
 * Immutable DTO record describing client readiness and connection status.
 */
public record CinemaStatusDto(
    String status,
    String ollamaModel,
    String mcpServerUrl,
    boolean mcpClientEnabled
) {}
