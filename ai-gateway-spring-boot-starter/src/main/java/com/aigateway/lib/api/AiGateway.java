package com.aigateway.lib.api;

import reactor.core.publisher.Flux;

public interface AiGateway {

    Flux<String> chat(String prompt, String modelOverride);
}