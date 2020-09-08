package com.pinguin.scrum.issue.controller;

import com.pinguin.scrum.developer.repository.entity.Developer;
import com.pinguin.scrum.developer.service.DeveloperService;
import com.pinguin.scrum.issue.repository.SprintRepository;
import com.pinguin.scrum.issue.service.IssueService;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        value = {"spring.profiles.active=test"},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@DisplayName("Sprint operations")
public class SprintControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DeveloperService developerService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private SprintRepository sprintRepository;

    private void createStories() {
        Stream.from(1L)
                .take(10)
                .forEach(l ->
                        issueService.createStory(
                                String.format("Story %d", l), String.format("Story %d description", l), l)
                );
    }

    @Nested
    @DisplayName("When one developer exist")
    class WhenOneDeveloper {

        private Developer dev;

        @BeforeEach
        void setUp() {
            dev = developerService.create("developer 1").get();
        }

        @AfterEach
        void tearDown() {
            sprintRepository.deleteAll();
            issueService.getAllStories().forEach(s -> issueService.deleteStory(s.getId()));
            developerService.delete(dev.getId()).get();
        }

        @Test
        @DisplayName("should return empty planning")
        public void testNoStories() throws Exception {
            mvc.perform(
                    post("/sprint/plan")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$").isEmpty());

            mvc.perform(
                    get("/sprint")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        @DisplayName("should return six week planning")
        public void testTenStories() throws Exception {
            createStories();
            MvcResult postResult =
                    mvc.perform(
                            post("/sprint/plan")
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isCreated())
                            .andExpect(jsonPath("$").isArray())
                            .andExpect(jsonPath("$").value(hasSize(6)))
                            .andReturn();

            MvcResult getResult =
                    mvc.perform(
                            get("/sprint")
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$").isArray())
                            .andExpect(jsonPath("$").value(hasSize(6)))
                            .andReturn();

            assertEquals(postResult.getResponse().getContentAsString(), getResult.getResponse().getContentAsString());
        }

    }

    @Nested
    @DisplayName("When two developers exist")
    class WhenTwoDevelopers {

        private List<Developer> devs;

        @BeforeEach
        void setUp() {
            devs = List.of(
                    developerService.create("developer 1").get(),
                    developerService.create("developer 2").get()
            );
        }

        @AfterEach
        void tearDown() {
            sprintRepository.deleteAll();
            issueService.getAllStories().forEach(s -> issueService.deleteStory(s.getId()));
            devs.forEach(d -> developerService.delete(d.getId()).get());
        }

        @Test
        @DisplayName("should return empty planning")
        public void testNoStories() throws Exception {
            mvc.perform(
                    post("/sprint/plan")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$").isEmpty());

            mvc.perform(
                    get("/sprint")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        @DisplayName("should return tree week planning")
        public void testTenStories() throws Exception {
            createStories();
            MvcResult postResult =
                    mvc.perform(
                            post("/sprint/plan")
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isCreated())
                            .andExpect(jsonPath("$").isArray())
                            .andExpect(jsonPath("$").value(hasSize(3)))
                            .andReturn();

            MvcResult getResult =
                    mvc.perform(
                            get("/sprint")
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$").isArray())
                            .andExpect(jsonPath("$").value(hasSize(3)))
                            .andReturn();

            assertEquals(postResult.getResponse().getContentAsString(), getResult.getResponse().getContentAsString());
        }

    }


}
