package com.pinguin.scrum.issue.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.pinguin.scrum.issue.model.WeeklyPlaning;
import com.pinguin.scrum.issue.repository.entity.Story;
import io.vavr.collection.List;

import java.util.Map;

public class WeeklyPlanningResponse {

    @JsonProperty("week_number")
    public final Integer weekNumber;

    @JsonProperty("total_story_points")
    public final Integer totalStoryPoints;

    @JsonProperty("assigned_stories")
    public final Map<String, java.util.List<StoryResponse>> assignedStories;

    private WeeklyPlanningResponse(Integer weekNumber, Integer totalStoryPoints, Map<String, java.util.List<StoryResponse>> assignedStories) {
        this.weekNumber = weekNumber;
        this.totalStoryPoints = totalStoryPoints;
        this.assignedStories = assignedStories;
    }

    public static List<WeeklyPlanningResponse> fromWeeklyPlanning(List<WeeklyPlaning> planingList) {
        return planingList
                .groupBy(w -> w.weekNumber)
                .map(t -> new WeeklyPlanningResponse(
                            t._1,
                            t._2.flatMap(w -> w.assignments).map(Story::getEstimation).sum().intValue(),
                            t._2.toMap(k -> k.developer.getName(),
                                       v -> v.assignments.map(StoryResponse::fromEntity).toJavaList()).toJavaMap()))
                .toList();
    }
}
