package org.springframework.backend2exercise3.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class TaskForm {

    @NotBlank(message = "Titel är obligatorisk")
    @Size(min = 3, max = 100, message = "Titel måste vara mellan 3 och 100 tecken")
    private String title;

    @Size(max = 500, message = "Beskrivning får inte överstiga 500 tecken")
    private String description;

    @Size(max = 100, message = "Tilldelad användare får inte vara längre än 100 tecken")
    private String assignee;

    @NotNull(message = "Plattform måste väljas")
    private String platform = "jira";

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;


    private String priority;
    private String category;

    // Konstruktorer
    public TaskForm() {
        this.priority = "Medium"; // Standardprioritet
    }

    public TaskForm(String title, String description) {
        this();
        this.title = title;
        this.description = description;
    }

    public TaskForm(String title, String description, String priority, String category) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.category = category;
    }

    // Getters och Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Utility-metoder
    public boolean hasDescription() {
        return description != null && !description.trim().isEmpty();
    }

    public boolean hasCategory() {
        return category != null && !category.trim().isEmpty();
    }

    public boolean isJira() {
        return "jira".equalsIgnoreCase(platform);
    }

    public boolean isTrello() {
        return "trello".equalsIgnoreCase(platform);
    }

    public boolean hasAssignee() {
        return assignee != null && !assignee.trim().isEmpty();
    }

    public boolean hasDueDate() {
        return dueDate != null;
    }

    // toString för debugging
    @Override
    public String toString() {
        return "TaskForm{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority='" + priority + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}