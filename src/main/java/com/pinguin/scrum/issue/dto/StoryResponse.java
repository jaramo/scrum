package com.pinguin.scrum.issue.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pinguin.scrum.developer.repository.entity.Developer;
import com.pinguin.scrum.issue.repository.entity.Story;

import java.util.Optional;

public class StoryResponse {

    @JsonProperty("id")
    public final Long id;

    @JsonProperty("title")
    public final String title;

    @JsonProperty("description")
    public final String description;

    @JsonProperty("assigned_developer")
    public final Optional<String> assignedDeveloper;

    @JsonProperty("estimation")
    public final Long estimation;

    @JsonProperty("status")
    public final Story.Status status;

    private StoryResponse(
            Long id,
            String title,
            String description,
            Optional<String> assignedDeveloper,
            Long estimation,
            Story.Status status
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.assignedDeveloper = assignedDeveloper;
        this.estimation = estimation;
        this.status = status;
    }

    public static StoryResponse fromEntity(Story entity) {
        return new StoryResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                Optional.ofNullable(entity.getAssignedDeveloper()).map(Developer::getName),
                entity.getEstimation(),
                entity.getStatus()
        );
    }
}
