package com.aigateway.lib.routing;

import com.aigateway.lib.config.AiGatewayProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;

public class IntelligentRouter {

    private static final Logger log = LoggerFactory.getLogger(IntelligentRouter.class);

    private final ChatClient openAi;
    private final ChatClient ollama;
    private final AiGatewayProperties props;
    private final PromptClassifier classifier;

    public IntelligentRouter(ChatClient openAi,
                             ChatClient ollama,
                             AiGatewayProperties props,
                             PromptClassifier classifier) {
        this.openAi = openAi;
        this.ollama = ollama;
        this.props = props;
        this.classifier = classifier;
    }

    public ChatClient route(String prompt, String overrideModel) {

        //  1. Manual override
        if (overrideModel != null) {
            log.info("[AI ROUTER] Override model requested: {}", overrideModel);

            if ("openai".equalsIgnoreCase(overrideModel)) {
                log.info("[AI ROUTER] Routing → OPENAI (override)");
                return openAi;
            }

            if ("ollama".equalsIgnoreCase(overrideModel)) {
                log.info("[AI ROUTER] Routing → OLLAMA (override)");
                return ollama;
            }
        }

        //  2. Intent detection
        PromptClassifier.PromptType type = classifier.classify(prompt);
        log.info("[AI ROUTER] Detected intent: {}", type);

        switch (type) {
            case CODE:
            case ANALYTICAL:
                log.info("[AI ROUTER] Routing → OPENAI (high quality use-case)");
                return openAi;

            case CHAT:
                log.info("[AI ROUTER] Routing → OLLAMA (cheap chat)");
                return ollama;

            case SUMMARIZATION:
                log.info("[AI ROUTER] Routing → OPENAI (summarization)");
                return openAi;

            default:
                break;
        }

        //  3. Cost-based fallback
        int tokens = prompt.length() / 4;

        log.info("[AI ROUTER] Estimated tokens: {}", tokens);
        log.info("[AI ROUTER] Threshold: {}", props.getTokenThreshold());

        if (tokens > props.getTokenThreshold()) {
            log.info("[AI ROUTER] Routing → OPENAI (token threshold exceeded)");
            return openAi;
        }

        log.info("[AI ROUTER] Routing → OLLAMA (default cheap path)");
        return ollama;
    }
}