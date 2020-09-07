package com.pinguin.scrum.developer.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotBlank;

@JsonAutoDetect
public class DeveloperRequest {

    @NotBlank
    public final String name;

    @JsonCreator
    public DeveloperRequest(String name) {
        this.name = name;
    }

}
