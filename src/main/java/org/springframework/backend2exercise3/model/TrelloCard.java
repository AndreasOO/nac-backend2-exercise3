package org.springframework.backend2exercise3.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrelloCard {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("desc")
    private String description;

    @JsonProperty("idList")
    private String listId;

    @JsonProperty("idBoard")
    private String boardId;

    @JsonProperty("due")
    private String dueDate;

    @JsonProperty("dueComplete")
    private boolean dueComplete;

    @JsonProperty("closed")
    private boolean closed;

    @JsonProperty("url")
    private String url;

    @JsonProperty("shortUrl")
    private String shortUrl;

    @JsonProperty("dateLastActivity")
    private String dateLastActivity;

    @JsonProperty("labels")
    private List<TrelloLabel> labels;

    @JsonProperty("members")
    private List<TrelloMember> members;

    @JsonProperty("pos")
    private double position;

    // Constructors
    public TrelloCard() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getListId() { return listId; }
    public void setListId(String listId) { this.listId = listId; }

    public String getBoardId() { return boardId; }
    public void setBoardId(String boardId) { this.boardId = boardId; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public boolean isDueComplete() { return dueComplete; }
    public void setDueComplete(boolean dueComplete) { this.dueComplete = dueComplete; }

    public boolean isClosed() { return closed; }
    public void setClosed(boolean closed) { this.closed = closed; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getShortUrl() { return shortUrl; }
    public void setShortUrl(String shortUrl) { this.shortUrl = shortUrl; }

    public String getDateLastActivity() { return dateLastActivity; }
    public void setDateLastActivity(String dateLastActivity) { this.dateLastActivity = dateLastActivity; }

    public List<TrelloLabel> getLabels() { return labels; }
    public void setLabels(List<TrelloLabel> labels) { this.labels = labels; }

    public List<TrelloMember> getMembers() { return members; }
    public void setMembers(List<TrelloMember> members) { this.members = members; }

    public double getPosition() { return position; }
    public void setPosition(double position) { this.position = position; }
}
