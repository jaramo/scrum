package com.pinguin.scrum.issue.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@JsonAutoDetect
public class StoryRequest {

    @NotBlank
    public final String title;

    @NotBlank
    public final String description;

    @Min(value = 1)
    @NotNull
    public final Long estimation;

    @JsonCreator
    public StoryRequest(String title, String description, Long estimation) {
        this.title = title;
        this.description = description;
        this.estimation = estimation;
    }
}
