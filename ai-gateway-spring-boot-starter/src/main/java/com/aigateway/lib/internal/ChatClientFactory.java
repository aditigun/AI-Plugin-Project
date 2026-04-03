package com.aigateway.lib.internal;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ChatClientFactory {

    private final ChatModel openAiModel;
    private final ChatModel ollamaModel;

    public ChatClientFactory(
            @Qualifier("openAiChatModel") ChatModel openAiModel,
            @Qualifier("ollamaChatModel") ChatModel ollamaModel) {
        this.openAiModel = openAiModel;
        this.ollamaModel = ollamaModel;
    }



    public ChatClient openAi() {
        return ChatClient.create(openAiModel);
    }

    public ChatClient ollama() {
        return ChatClient.create(ollamaModel);
    }
}