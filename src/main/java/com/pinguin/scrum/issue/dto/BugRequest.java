package com.pinguin.scrum.issue.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.pinguin.scrum.issue.repository.entity.Bug.Priority;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@JsonAutoDetect
public class BugRequest {

    @NotBlank
    public final String title;

    @NotBlank
    public final String description;

    @NotNull
    public final Priority priority;

    @JsonCreator
    public BugRequest(String title, String description, Priority priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }
}
