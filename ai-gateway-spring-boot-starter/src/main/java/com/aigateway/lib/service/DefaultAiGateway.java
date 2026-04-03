package com.aigateway.lib.service;

import com.aigateway.lib.api.AiGateway;
import reactor.core.publisher.Flux;

public class DefaultAiGateway implements AiGateway {

    private final AiOrchestrator orchestrator;

    public DefaultAiGateway(AiOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @Override
    public Flux<String> chat(String prompt, String modelOverride) {
        return orchestrator.process(prompt, modelOverride);
    }
}