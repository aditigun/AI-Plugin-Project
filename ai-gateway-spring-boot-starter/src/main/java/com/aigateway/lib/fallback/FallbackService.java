package com.aigateway.lib.fallback;

import org.springframework.ai.chat.client.ChatClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class FallbackService {

    private final ChatClient fallback;
    private final boolean enabled;

    public FallbackService(ChatClient fallback, boolean enabled) {
        this.fallback = fallback;
        this.enabled = enabled;
    }

    public Flux<String> fallback(String prompt) {
        if (!enabled) return Flux.error(new RuntimeException("Fallback disabled"));

        return fallback.prompt(prompt)
                .stream()
                .content()
                .timeout(Duration.ofSeconds(15));
    }
}