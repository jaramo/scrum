package com.pinguin.scrum.issue.controller;

import com.pinguin.scrum.issue.dto.WeeklyPlanningResponse;
import com.pinguin.scrum.issue.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/sprint")
public class SprintController {

    @Autowired
    private SprintService sprintService;

    @RequestMapping(value = "", method = {RequestMethod.GET})
    @ResponseStatus(HttpStatus.OK)
    public List<WeeklyPlanningResponse> get() {
        return sprintService.get()
                .transform(WeeklyPlanningResponse::fromWeeklyPlanning)
                .toJavaList();
    }

    @RequestMapping(value = "/plan", method = {RequestMethod.POST})
    @ResponseStatus(HttpStatus.CREATED)
    public List<WeeklyPlanningResponse> plan() {
        return sprintService.plan()
                            .transform(WeeklyPlanningResponse::fromWeeklyPlanning)
                            .toJavaList();
    }
}
