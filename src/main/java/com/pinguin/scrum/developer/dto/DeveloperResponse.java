package com.pinguin.scrum.developer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pinguin.scrum.developer.repository.entity.Developer;
import com.pinguin.scrum.issue.dto.BugResponse;
import com.pinguin.scrum.issue.dto.StoryResponse;

import java.util.List;
import java.util.stream.Collectors;

public class DeveloperResponse {

    @JsonProperty("id")
    public final Long id;

    @JsonProperty("name")
    public final String name;

    @JsonProperty("assigned_bugs")
    public final List<BugResponse> assignedBugs;

    @JsonProperty("assigned_stories")
    public final List<StoryResponse> assignedStories;

    private DeveloperResponse(Long id,
                              String name,
                              List<BugResponse> assignedBugs,
                              List<StoryResponse> assignedStories) {
        this.id = id;
        this.name = name;
        this.assignedBugs = assignedBugs;
        this.assignedStories = assignedStories;
    }

    public static DeveloperResponse fromEntity(Developer developer) {
        return new DeveloperResponse(
                developer.getId(),
                developer.getName(),
                developer.getAssignedBugs().stream().map(BugResponse::fromEntity).collect(Collectors.toList()),
                developer.getAssignedStories().stream().map(StoryResponse::fromEntity).collect(Collectors.toList())
        );
    }
}
