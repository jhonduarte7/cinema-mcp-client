package com.venefast.springboot.cinemamcpclient.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration for the Spring AI ChatClient integrating Ollama and Model Context Protocol (MCP) tools.
 */
@Configuration
public class CinemaAssistantConfig {

    private static final Logger log = LoggerFactory.getLogger(CinemaAssistantConfig.class);

    private static final String DEFAULT_SYSTEM_PROMPT = """
        You are CineBot, an intelligent and polite cinema concierge assistant.
        You have direct access to real-time movie tools provided by the Movie MCP Server.
        Always leverage the available tools to search movies by title, filter by genre, check ratings,
        inspect screening schedules, and recommend movies.
        When providing answers, be concise, polite, and structure recommendations with movie title,
        release year, director, rating, and screening schedules when available.
        """;

    @Bean
    public ChatClient chatClient(
            ChatClient.Builder chatClientBuilder,
            ObjectProvider<List<ToolCallbackProvider>> toolCallbackProviders
    ) {
        var builder = chatClientBuilder.defaultSystem(DEFAULT_SYSTEM_PROMPT);

        List<ToolCallbackProvider> providers = toolCallbackProviders.getIfAvailable();
        if (providers != null && !providers.isEmpty()) {
            for (ToolCallbackProvider provider : providers) {
                log.info("Registering MCP ToolCallbackProvider into ChatClient: {}", provider.getClass().getSimpleName());
                builder.defaultToolCallbacks(provider.getToolCallbacks());
            }
        } else {
            log.warn("No MCP ToolCallbackProvider available at startup. Operating in direct LLM mode.");
        }

        return builder.build();
    }
}
