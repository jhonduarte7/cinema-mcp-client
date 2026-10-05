package com.venefast.springboot.cinemamcpclient.app.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Immutable DTO record representing a prompt request to the cinema assistant.
 */
public record CinemaPromptRequestDto(
    @NotBlank(message = "Prompt cannot be blank")
    @Size(max = 2000, message = "Prompt cannot exceed 2000 characters")
    String prompt,

    @Size(max = 200, message = "User preference cannot exceed 200 characters")
    String userPreference
) {
    public CinemaPromptRequestDto(String prompt) {
        this(prompt, null);
    }
}
