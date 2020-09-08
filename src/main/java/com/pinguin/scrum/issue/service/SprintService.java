package com.pinguin.scrum.issue.service;

import com.pinguin.scrum.developer.repository.entity.Developer;
import com.pinguin.scrum.developer.service.DeveloperService;
import com.pinguin.scrum.issue.model.WeeklyPlaning;
import com.pinguin.scrum.issue.repository.SprintRepository;
import com.pinguin.scrum.issue.repository.entity.Sprint;
import com.pinguin.scrum.issue.repository.entity.Story;
import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;

import java.util.function.BiFunction;

import static io.vavr.API.*;
import static io.vavr.Patterns.$Cons;
import static io.vavr.Patterns.$Nil;

public class SprintService {

    private final SprintRepository sprintRepository;
    private final DeveloperService developerService;
    private final IssueService issueService;
    private final Long capacity;

    public SprintService(
        SprintRepository sprintRepository,
        DeveloperService developerService,
        IssueService issueService,
        Long capacity
    ) {
        this.sprintRepository = sprintRepository;
        this.developerService = developerService;
        this.issueService = issueService;
        this.capacity = capacity;
    }

    public List<WeeklyPlaning> get() {
        return groupFlatten(List.ofAll(sprintRepository.findAll()), Sprint::getAssignedDeveloper,
                (dev, x) -> groupFlatten(x, Sprint::getWeekNumber,
                (week, y) -> List.of(y.foldLeft(new WeeklyPlaning(week, dev, this.capacity, List.empty()),
                (acc, s) -> acc.copy(acc.capacity - s.getStory().getEstimation(), acc.assignments.append(s.getStory()))))));
    }

    public List<WeeklyPlaning> plan() {
        List<Developer> devs = List.ofAll(this.developerService.getAllDevelopers());
        List<Story> stories = List.ofAll(issueService.getAllStories()).sortBy(Story::getEstimation).reverse();

        List<WeeklyPlaning> weeklyPlanning = this.plan(1, stories, List.empty(), devs);
        this.commit(weeklyPlanning);

        return weeklyPlanning;
    }

    private <T, U, R> List<R> groupFlatten(List<T> list, Function1<T, U> classifier, BiFunction<U, List<T>, List<R>> f) {
        return list.groupBy(classifier)
                .toList()
                .flatMap(tuple -> f.apply(tuple._1, tuple._2));
    }

    private void commit(List<WeeklyPlaning> planing) {
        sprintRepository.deleteAll();
        planing.flatMap(
                    w -> w.assignments.map(
                    s -> new Sprint(w.weekNumber, w.developer, s)))
               .transform(sprintRepository::saveAll)
               .forEach(s -> this.issueService.assign(s.getStory(), s.getAssignedDeveloper()));
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
