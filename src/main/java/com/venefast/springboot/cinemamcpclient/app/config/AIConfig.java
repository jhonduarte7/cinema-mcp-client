package com.venefast.springboot.cinemamcpclient.app.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ObjectProvider<ToolCallbackProvider> toolCallbackProvider) {
        toolCallbackProvider.ifAvailable(builder::defaultTools);
        return builder.build();
    }

}
