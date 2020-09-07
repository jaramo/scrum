package com.pinguin.scrum.issue.service;

import com.pinguin.scrum.developer.repository.entity.Developer;
import com.pinguin.scrum.developer.service.DeveloperService;
import com.pinguin.scrum.issue.model.WeeklyPlaning;
import com.pinguin.scrum.issue.repository.entity.Story;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Plan weekly stories assignation to developers")
public class SprintServiceTest {

    @Mock
    private IssueService issueService;

    private final Long developerCapacityPerWeek = 10L;

    @BeforeEach
    void setUp() {
        reset(issueService);
        when(issueService.getAllStories()).thenReturn(List.of(
                new Story("story6", "desc6", 6L),
                new Story("story2", "desc2", 2L),
                new Story("story4", "desc4", 4L),
                new Story("story1", "desc1", 1L),
                new Story("story5", "desc5", 5L),
                new Story("story3", "desc3", 3L)
        ));
    }

    @Test
    @DisplayName("6 stories with 21 points for one developer should be scheduled in 3 weeks")
    void testPlanSprintOnDeveloper(@Mock DeveloperService developerService) {

        Developer developer = new Developer("dev1");
        when(developerService.getAllDevelopers()).thenReturn(List.of(developer));

        SprintService sprintService = new SprintService(developerService, issueService, developerCapacityPerWeek);
        Map<Developer, List<WeeklyPlaning>> plan = sprintService.plan()
                                                                .groupBy(w -> w.developer)
                                                                .toMap(x -> x._1, t -> t._2);

        List<WeeklyPlaning> result = plan.getOrElse(developer, List.empty()).sortBy(x -> x.weekNumber);

        assertAll(
                () -> assertEquals(3, result.length(), "total"),

                () -> assertEquals(1, result.head().weekNumber, "week number 1"),
                () -> assertEquals(0, result.head().capacity, "capacity 0"),
                () -> assertEquals(2, result.head().assignments.length(), "number of stories 2"),

                () -> assertEquals(2, result.tail().head().weekNumber, "week number 2"),
                () -> assertEquals(0, result.tail().head().capacity, "capacity 0"),
                () -> assertEquals(3, result.tail().head().assignments.length(), "number of stories 3"),

                () -> assertEquals(3, result.tail().tail().head().weekNumber, "week number 3"),
                () -> assertEquals(9, result.tail().tail().head().capacity, "capacity 9"),
                () -> assertEquals(1, result.tail().tail().head().assignments.length(), "number of stories 1")
        );
    }
    
    @Test
    @DisplayName("6 stories with 21 points for 2 developers should be scheduled in 2 weeks")
    void testPlanSprintTwoDevelopers(@Mock DeveloperService developerService) {

        Developer dev1 = new Developer("dev1");
        Developer dev2 = new Developer("dev2");
        when(developerService.getAllDevelopers()).thenReturn(List.of(dev1, dev2));

        SprintService sprintService = new SprintService(developerService, issueService, developerCapacityPerWeek);
        Map<Developer, List<WeeklyPlaning>> plan = sprintService.plan()
                                                                .groupBy(w -> w.developer)
                                                                .toMap(x -> x._1, t -> t._2);

        List<WeeklyPlaning> planningDev1 = plan.getOrElse(dev1, List.empty()).sortBy(x -> x.weekNumber);
        assertAll(
                () -> assertEquals(2, planningDev1.length(), "total"),

                () -> assertEquals(1, planningDev1.head().weekNumber, "week number 1"),
                () -> assertEquals(0, planningDev1.head().capacity, "capacity 0"),
                () -> assertEquals(2, planningDev1.head().assignments.length(), "number of stories 2"),

                () -> assertEquals(2, planningDev1.tail().head().weekNumber, "week number 2"),
                () -> assertEquals(9, planningDev1.tail().head().capacity, "capacity 9"),
                () -> assertEquals(1, planningDev1.tail().head().assignments.length(), "number of stories 1")
        );

        List<WeeklyPlaning> planningDev2 = plan.getOrElse(dev2, List.empty()).sortBy(x -> x.weekNumber);
        assertAll(
                () -> assertEquals(1, planningDev2.length(), "total"),

                () -> assertEquals(1, planningDev2.head().weekNumber, "week number 1"),
                () -> assertEquals(0, planningDev2.head().capacity, "capacity 0"),
                () -> assertEquals(3, planningDev2.head().assignments.length(), "number of stories 2")
        );
    }
}
