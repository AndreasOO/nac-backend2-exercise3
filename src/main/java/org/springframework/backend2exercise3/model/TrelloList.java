package org.springframework.backend2exercise3.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrelloList {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("closed")
    private boolean closed;

    @JsonProperty("idBoard")
    private String boardId;

    @JsonProperty("pos")
    private double position;

    // Constructors
    public TrelloList() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isClosed() { return closed; }
    public void setClosed(boolean closed) { this.closed = closed; }

    public String getBoardId() { return boardId; }
    public void setBoardId(String boardId) { this.boardId = boardId; }

    public double getPosition() { return position; }
    public void setPosition(double position) { this.position = position; }
}
