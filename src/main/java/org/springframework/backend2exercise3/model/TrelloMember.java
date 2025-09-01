package org.springframework.backend2exercise3.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrelloMember {

    @JsonProperty("id")
    private String id;

    @JsonProperty("fullName")
    private String fullName;

    @JsonProperty("username")
    private String username;

    @JsonProperty("avatarUrl")
    private String avatarUrl;

    // Constructors
    public TrelloMember() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}
