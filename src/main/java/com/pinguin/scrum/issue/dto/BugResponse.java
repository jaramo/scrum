package com.pinguin.scrum.issue.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pinguin.scrum.developer.repository.entity.Developer;
import com.pinguin.scrum.issue.repository.entity.Bug;

import java.util.Optional;

public class BugResponse {

    @JsonProperty("id")
    public final Long id;

    @JsonProperty("title")
    public final String title;

    @JsonProperty("description")
    public final String description;

    @JsonProperty("assigned_developer")
    public final Optional<String> assignedDeveloper;

    @JsonProperty("priority")
    public final Bug.Priority priority;

    @JsonProperty("status")
    public final Bug.Status status;

    private BugResponse(
         Long id,
         String title,
         String description,
         Optional<String> assignedDeveloper,
         Bug.Priority priority,
         Bug.Status status
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.assignedDeveloper = assignedDeveloper;
        this.priority = priority;
        this.status = status;
    }

    public static BugResponse fromEntity(Bug bug) {
        return new BugResponse(
            bug.getId(),
            bug.getTitle(),
            bug.getDescription(),
            Optional.ofNullable(bug.getAssignedDeveloper()).map(Developer::getName),
            bug.getPriority(),
            bug.getStatus()
        );
    }
}
