package com.pinguin.scrum.issue.controller;

import com.pinguin.scrum.issue.repository.BugRepository;
import com.pinguin.scrum.issue.repository.StoryRepository;
import com.pinguin.scrum.issue.repository.entity.Bug;
import com.pinguin.scrum.issue.repository.entity.Story;
import com.pinguin.scrum.util.TestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
    value={"spring.profiles.active=test"},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@DisplayName("Issues CRUD operations")
public class IssueControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BugRepository bugRepository;

    @Autowired
    private StoryRepository storyRepository;
    
    @AfterEach
    void tearDown() {
        bugRepository.deleteAll();
        storyRepository.deleteAll();
    }

    @Nested
    @DisplayName("Bugs CRUD operations")
    class BugsOperations {

        @Nested
        @DisplayName("When no bugs")
        class WhenNoBugs {

            @Test
            @DisplayName("should return empty list")
            public void testGetAll() throws Exception {
                mvc.perform(
                        get("/issues/bugs")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isEmpty());
            }

            @Test
            @DisplayName("should return not found")
            public void testGet() throws Exception {
                mvc.perform(
                        get("/issues/bugs/1")
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }

            @Test
            @DisplayName("should return created")
            public void testCreate() throws Exception {
                mvc.perform(
                        post("/issues/bugs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.loadFile("CreateBug.json")))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id").isNotEmpty())
                        .andExpect(jsonPath("$.title").isNotEmpty())
                        .andExpect(jsonPath("$.description").isNotEmpty())
                        .andExpect(jsonPath("$.assigned_developer").isEmpty())
                        .andExpect(jsonPath("$.priority").value("MINOR"))
                        .andExpect(jsonPath("$.status").value("NEW"));
            }

            @Test
            @DisplayName("should return bad request")
            public void testCreateBadRequest() throws Exception {
                mvc.perform(
                        post("/issues/bugs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.loadFile("CreateBugBadRequest.json")))
                        .andExpect(status().isBadRequest())
                ;
            }

            @Test
            @DisplayName("should return error in deletion")
            public void testDeleteNotExisting() throws Exception {
                mvc.perform(
                        delete("/issues/bugs/1")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("When bugs exists")
        class WhenBugsExists {

            private Bug bug;

            @BeforeEach
            void setUp() {
                this.bug = bugRepository.save(
                        new Bug("random bug title", "random bug description", Bug.Priority.MINOR));
            }

            @Test
            @DisplayName("should return list with one element")
            public void testGetAll() throws Exception {
                mvc.perform(
                        get("/issues/bugs")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$").value(hasSize(1)));
            }

            @Test
            @DisplayName("should return bug")
            public void testGet() throws Exception {
                mvc.perform(
                        get(String.format("/issues/bugs/%d", bug.getId()))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(bug.getId()))
                        .andExpect(jsonPath("$.title").value(bug.getTitle()))
                        .andExpect(jsonPath("$.description").value(bug.getDescription()))
                        .andExpect(jsonPath("$.assigned_developer").isEmpty())
                        .andExpect(jsonPath("$.priority").value(bug.getPriority().toString()))
                        .andExpect(jsonPath("$.status").value(bug.getStatus().toString()))
                ;
            }

            @Test
            @DisplayName("should be able to delete it")
            public void testDelete() throws Exception {
                mvc.perform(
                        delete(String.format("/issues/bugs/%d", bug.getId()))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());
            }

            @Test
            @DisplayName("should return bug already exists error")
            public void testCreateDuplicated() throws Exception {
                mvc.perform(
                        post("/issues/bugs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{" +
                                        "\"title\":\"%s\"," +
                                        "\"description\":\"%s\"," +
                                        "\"priority\":\"%s\"" +
                                    "}", bug.getTitle(), bug.getDescription(), bug.getPriority())))
                        .andExpect(status().isConflict())
                        .andExpect(status().reason("Duplicated title"));
            }
        }
    }

    @Nested
    @DisplayName("Stories CRUD operations")
    class StoriesOperations {

        @Nested
        @DisplayName("When no stories")
        class WhenNoStories {

            @Test
            @DisplayName("should return empty list")
            public void testGetAll() throws Exception {
                mvc.perform(
                        get("/issues/stories")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isEmpty());
            }

            @Test
            @DisplayName("should return not found")
            public void testGet() throws Exception {
                mvc.perform(
                        get("/issues/stories/1")
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }

            @Test
            @DisplayName("should return created")
            public void testCreate() throws Exception {
                mvc.perform(
                        post("/issues/stories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.loadFile("CreateStory.json")))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id").isNotEmpty())
                        .andExpect(jsonPath("$.title").isNotEmpty())
                        .andExpect(jsonPath("$.description").isNotEmpty())
                        .andExpect(jsonPath("$.assigned_developer").isEmpty())
                        .andExpect(jsonPath("$.estimation").isNotEmpty())
                        .andExpect(jsonPath("$.status").value("NEW"));
            }

            @Test
            @DisplayName("should return bad request")
            public void testCreateBadRequest() throws Exception {
                mvc.perform(
                        post("/issues/stories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.loadFile("CreateStoryBadRequest.json")))
                        .andExpect(status().isBadRequest())
                ;
            }

            @Test
            @DisplayName("should return error in deletion")
            public void testDeleteNotExisting() throws Exception {
                mvc.perform(
                        delete("/issues/stories/1")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("When stories exists")
        class WhenStoriesExists {

            private Story story;

            @BeforeEach
            void setUp() {
                this.story = storyRepository.save(
                        new Story("random story title", "random story description", 3L)); }

            @Test
            @DisplayName("should return list with one element")
            public void testGetAll() throws Exception {
                mvc.perform(
                        get("/issues/stories")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$").value(hasSize(1)));
            }

            @Test
            @DisplayName("should return bug")
            public void testGet() throws Exception {
                mvc.perform(
                        get(String.format("/issues/stories/%d", story.getId()))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(story.getId()))
                        .andExpect(jsonPath("$.title").value(story.getTitle()))
                        .andExpect(jsonPath("$.description").value(story.getDescription()))
                        .andExpect(jsonPath("$.assigned_developer").isEmpty())
                        .andExpect(jsonPath("$.estimation").value(story.getEstimation()))
                        .andExpect(jsonPath("$.status").value(story.getStatus().toString()))
                ;
            }

            @Test
            @DisplayName("should be able to delete it")
            public void testDelete() throws Exception {
                mvc.perform(
                        delete(String.format("/issues/stories/%d", story.getId()))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());
            }

            @Test
            @DisplayName("should return bug already exists error")
            public void testCreateDuplicated() throws Exception {
                mvc.perform(
                        post("/issues/stories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{" +
                                        "\"title\":\"%s\"," +
                                        "\"description\":\"%s\"," +
                                        "\"estimation\":\"%s\"" +
                                        "}", story.getTitle(), story.getDescription(), story.getEstimation())))
                        .andExpect(status().isConflict())
                        .andExpect(status().reason("Duplicated title"));
            }
        }
    }

}
