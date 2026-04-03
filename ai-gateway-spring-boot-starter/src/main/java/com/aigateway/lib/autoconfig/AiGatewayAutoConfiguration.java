package com.aigateway.lib.autoconfig;

import com.aigateway.lib.api.AiGateway;
import com.aigateway.lib.config.AiGatewayProperties;
import com.aigateway.lib.fallback.FallbackService;
import com.aigateway.lib.internal.ChatClientFactory;
import com.aigateway.lib.rag.RagService;
import com.aigateway.lib.routing.IntelligentRouter;
import com.aigateway.lib.routing.PromptClassifier;
import com.aigateway.lib.service.AiOrchestrator;
import com.aigateway.lib.service.DefaultAiGateway;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

@Configuration
@EnableConfigurationProperties(AiGatewayProperties.class)
public class AiGatewayAutoConfiguration {

    @Bean
    public PromptClassifier classifier() {
        return new PromptClassifier();
    }

    @Bean
    public ChatClientFactory chatClientFactory(
            @Qualifier("openAiChatModel") ChatModel openAiChatModel,
            @Qualifier("ollamaChatModel") ChatModel ollamaChatModel) {

        return new ChatClientFactory(openAiChatModel, ollamaChatModel);
    }

    @Bean
    public IntelligentRouter router(ChatClientFactory factory,
                                    AiGatewayProperties props,
                                    PromptClassifier classifier) {
        return new IntelligentRouter(factory.openAi(), factory.ollama(), props, classifier);
    }

    @Bean
    public FallbackService fallback(ChatClientFactory factory,
                                    AiGatewayProperties props) {
        return new FallbackService(factory.ollama(), props.isFallbackEnabled());
    }

    @Bean
    public RagService rag(VectorStore store,
                          AiGatewayProperties props) {
        return new RagService(store, props);
    }

    @Bean
    public AiOrchestrator orchestrator(RagService rag,
                                       IntelligentRouter router,
                                       FallbackService fallback) {
        return new AiOrchestrator(rag, router, fallback);
    }

    @Bean
    public AiGateway aiGateway(AiOrchestrator orchestrator) {
        return new DefaultAiGateway(orchestrator);
    }
}