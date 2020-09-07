package com.pinguin.scrum.developer.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pinguin.scrum.developer.repository.entity.Developer;
import com.pinguin.scrum.issue.dto.BugResponse;
import com.pinguin.scrum.issue.dto.StoryResponse;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@JsonAutoDetect
public class DeveloperResponse {

    public final Long id;

    public final String name;

    public final List<BugResponse> assignedBugs;

    public final List<StoryResponse> assignedStories;

    @JsonCreator
    private DeveloperResponse(
            @JsonProperty("id") Long id,
            @JsonProperty("name") @NotEmpty String name,
            @JsonProperty("assigned_bugs") List<BugResponse> assignedBugs,
            @JsonProperty("assigned_stories") List<StoryResponse> assignedStories
    ) {
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
