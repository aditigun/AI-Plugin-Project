package com.aigateway.lib.config;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "ai.gateway")
@Validated
public class AiGatewayProperties {

    private boolean ragEnabled = true;
    private boolean fallbackEnabled = true;

    @Min(1)
    private int tokenThreshold = 500;

    public boolean isRagEnabled() {
        return ragEnabled;
    }

    public void setRagEnabled(boolean ragEnabled) {
        this.ragEnabled = ragEnabled;
    }

    public boolean isFallbackEnabled() {
        return fallbackEnabled;
    }

    public void setFallbackEnabled(boolean fallbackEnabled) {
        this.fallbackEnabled = fallbackEnabled;
    }

    public int getTokenThreshold() {
        return tokenThreshold;
    }

    public void setTokenThreshold(int tokenThreshold) {
        this.tokenThreshold = tokenThreshold;
    }

    /**
     *  VALIDATION + LOGGING AT STARTUP
     */
    @PostConstruct
    public void validateAndLog() {

        if (tokenThreshold <= 0) {
            throw new IllegalStateException("ai.gateway.token-threshold must be > 0");
        }

        System.out.println("========== AI GATEWAY CONFIG ==========");
        System.out.println("RAG Enabled        : " + ragEnabled);
        System.out.println("Fallback Enabled   : " + fallbackEnabled);
        System.out.println("Token Threshold    : " + tokenThreshold);
        System.out.println("=======================================");
    }
}