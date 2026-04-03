package com.aigateway.lib.rag;

import com.aigateway.lib.config.AiGatewayProperties;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;

public class RagService {

    private final VectorStore vectorStore;
    private final AiGatewayProperties props;

    public RagService(VectorStore vectorStore, AiGatewayProperties props) {
        this.vectorStore = vectorStore;
        this.props = props;
    }

    public String enrich(String prompt) {

        if (!props.isRagEnabled()) {
            return prompt;
        }

        // 🔥 THIS WAS MISSING
        List<Document> docs = vectorStore.similaritySearch(prompt);

        StringBuilder ctx = new StringBuilder();

        for (Document d : docs) {
            ctx.append(d.getText()).append("\n");
        }

        return "Context:\n" + ctx + "\n\nQuestion:\n" + prompt;
    }
}