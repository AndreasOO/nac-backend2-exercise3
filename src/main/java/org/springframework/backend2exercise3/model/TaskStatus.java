package org.springframework.backend2exercise3.model;

public enum TaskStatus {

    TODO("To Do"),
    IN_PROGRESS("In Progress"),
    IN_REVIEW("In Review"),
    DONE("Done"),
    ARCHIVED("Archived");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
