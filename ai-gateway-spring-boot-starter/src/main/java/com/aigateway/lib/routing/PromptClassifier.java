package com.aigateway.lib.routing;

public class PromptClassifier {

    public enum PromptType {
        CODE,
        CHAT,
        SUMMARIZATION,
        ANALYTICAL,
        UNKNOWN
    }

    public PromptType classify(String prompt) {

        String p = prompt.toLowerCase();

        if (p.contains("code") || p.contains("java") || p.contains("sql") || p.contains("function")) {
            return PromptType.CODE;
        }

        if (p.contains("summarize") || p.contains("summary")) {
            return PromptType.SUMMARIZATION;
        }

        if (p.contains("analyze") || p.contains("compare")) {
            return PromptType.ANALYTICAL;
        }

        if (p.length() < 200) {
            return PromptType.CHAT;
        }

        return PromptType.UNKNOWN;
    }
}