package com.aigateway.lib.service;

import com.aigateway.lib.fallback.FallbackService;
import com.aigateway.lib.rag.RagService;
import com.aigateway.lib.routing.IntelligentRouter;
import org.springframework.ai.chat.client.ChatClient;
import reactor.core.publisher.Flux;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AiOrchestrator {
	
	private static final Logger log = LoggerFactory.getLogger(AiOrchestrator.class);

    private final RagService rag;
    private final IntelligentRouter router;
    private final FallbackService fallback;

    public AiOrchestrator(RagService rag,
                          IntelligentRouter router,
                          FallbackService fallback) {
        this.rag = rag;
        this.router = router;
        this.fallback = fallback;
    }

    public Flux<String> process(String prompt, String modelOverride) {

        String enriched = rag.enrich(prompt);

        ChatClient client = router.route(enriched, modelOverride);

        return client.prompt(enriched)
                .stream()
                .content()
                .onErrorResume(ex -> fallback.fallback(enriched));
    }
}