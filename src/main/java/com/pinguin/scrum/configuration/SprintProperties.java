package com.pinguin.scrum.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;


@ConfigurationProperties(value = "sprint")
@Validated
public class SprintProperties {

    @Min(value = 1)
    private Long developerCapacityPerWeek;

    public Long getDeveloperCapacityPerWeek() {
        return developerCapacityPerWeek;
    }

    public void setDeveloperCapacityPerWeek(Long developerCapacityPerWeek) {
        this.developerCapacityPerWeek = developerCapacityPerWeek;
    }
}
