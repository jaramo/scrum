package com.pinguin.scrum.issue.service;

import com.pinguin.scrum.developer.repository.entity.Developer;
import com.pinguin.scrum.developer.service.DeveloperService;
import com.pinguin.scrum.issue.model.WeeklyPlaning;
import com.pinguin.scrum.issue.repository.entity.Story;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;

import static io.vavr.API.*;
import static io.vavr.Patterns.$Cons;
import static io.vavr.Patterns.$Nil;

public class SprintService {

    private final DeveloperService developerService;
    private final IssueService issueService;
    private final Long capacity;

    public SprintService(DeveloperService developerService, IssueService issueService, Long capacity) {
        this.developerService = developerService;
        this.issueService = issueService;
        this.capacity = capacity;
    }

    public List<WeeklyPlaning> plan() {
        List<Developer> devs = List.ofAll(this.developerService.getAllDevelopers());
        List<Story> stories = List.ofAll(issueService.getAllStories()).sortBy(Story::getEstimation).reverse();

        return this.plan(1, stories, List.empty(), devs);
    }

    private List<WeeklyPlaning> plan(Integer week, List<Story> unplannedStories, List<WeeklyPlaning> planning, List<Developer> devs) {
        if (unplannedStories.isEmpty()) {
            return planning;
        }

        return devs.foldLeft(Tuple.of(unplannedStories, planning),
            (acc, developer) -> {
                if (acc._1.isEmpty()) {
                    return acc;
                } else {
                    return this.getWeeklyPlanningForDeveloper(week, developer, acc._1).map(x -> x, acc._2::append);
                }
            }
        )
        .apply((xs, ps) -> this.plan(week + 1, xs, ps, devs));
    }

    private Tuple2<List<Story>, WeeklyPlaning> getWeeklyPlanningForDeveloper(Integer week, Developer developer, List<Story> unplannedStories) {
        return this.addStoriesToWeeklyPlanning(
                unplannedStories, new WeeklyPlaning(week, developer, this.capacity, List.empty()));
    }

    private Tuple2<List<Story>, WeeklyPlaning> addStoriesToWeeklyPlanning(List<Story> stories, WeeklyPlaning p) {
        return Match(stories).of(
                Case($Cons($(s -> s.getEstimation() <= p.capacity), $()),
                        (x, xs) -> this.addStoriesToWeeklyPlanning(xs, p.copy(p.capacity - x.getEstimation(), p.assignments.append(x)))),

                Case($Cons($(), $()),
                        (x, xs) -> this.addStoriesToWeeklyPlanning(xs, p).apply((u, r) -> Tuple.of(u.prepend(x), r))),

                Case($Nil(), () -> Tuple.of(stories, p))
        );
    }

}
