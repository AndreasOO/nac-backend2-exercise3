package org.springframework.backend2exercise3.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "trello.api")
public class TrelloConfig {
    private String baseUrl;
    private String key;
    private String token;
    private String boardId;
    private String defaultListId;

    // Getters and Setters
    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getDefaultListId() {
        return defaultListId;
    }

    public void setDefaultListId(String defaultListId) {
        this.defaultListId = defaultListId;
    }

    public boolean hasDefaultListId() {
        return defaultListId != null && !defaultListId.trim().isEmpty();
    }
}
