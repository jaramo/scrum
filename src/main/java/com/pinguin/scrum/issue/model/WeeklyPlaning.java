package com.pinguin.scrum.issue.model;

import com.pinguin.scrum.developer.repository.entity.Developer;
import com.pinguin.scrum.issue.repository.entity.Story;
import io.vavr.collection.List;

public class WeeklyPlaning {

    public final Integer weekNumber;
    public final Developer developer;
    public final Long capacity;
    public final List<Story> assignments;

    public WeeklyPlaning(Integer weekNumber, Developer developer, Long capacity, List<Story> assignments) {
        this.weekNumber = weekNumber;
        this.developer = developer;
        this.capacity = capacity;
        this.assignments = assignments;
    }

    public WeeklyPlaning copy(Long capacity, List<Story> assignments) {
        return new WeeklyPlaning(
            this.weekNumber,
            this.developer,
            capacity,
            assignments
        );
    }

}
