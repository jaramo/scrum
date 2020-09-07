package com.pinguin.scrum.developer.controller;

import com.pinguin.scrum.developer.repository.DeveloperRepository;
import com.pinguin.scrum.developer.repository.entity.Developer;
import com.pinguin.scrum.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
@DisplayName("Developer CRUD operations")
public class DeveloperControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DeveloperRepository developerRepository;

    @BeforeEach
    void setUp() {
        developerRepository.deleteAll();
    }

    @Nested
    @DisplayName("When no developers")
    class WhenNoDevelopers {
        @Test
        @DisplayName("should return empty list")
        public void testGetAll() throws Exception {
            mvc.perform(
                    get("/developers")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty())
            ;
        }

        @Test
        @DisplayName("should return not found")
        public void testGet() throws Exception {
            mvc.perform(
                    get("/developers/1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("should return created")
        public void testCreate() throws Exception {
            mvc.perform(
                    post("/developers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.loadFile("CreateDeveloper.json")))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.name").isNotEmpty())
                    .andExpect(jsonPath("$.assigned_bugs").isArray())
                    .andExpect(jsonPath("$.assigned_bugs").value(hasSize(0)))
                    .andExpect(jsonPath("$.assigned_stories").isArray())
                    .andExpect(jsonPath("$.assigned_stories").value(hasSize(0)))
            ;
        }

        @Test
        @DisplayName("should return bad request")
        public void testCreateBadRequest() throws Exception {
            mvc.perform(
                    post("/developers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.loadFile("CreateDeveloperBadRequest.json")))
                    .andExpect(status().isBadRequest())
            ;
        }

        @Test
        @DisplayName("should return error in deletion")
        public void testDeleteNotExisting() throws Exception {
            mvc.perform(
                    delete("/developers/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("When developer exists")
    class WhenDeveloperExists {

        private Developer developer;

        @BeforeEach
        void setUp() {
            this.developer = developerRepository.save(new Developer("Test Developer"));
        }

        @Test
        @DisplayName("should return list with one element")
        public void testGetAll() throws Exception {
            mvc.perform(
                    get("/developers")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$", hasSize(1)))
            ;
        }

        @Test
        @DisplayName("should return developer")
        public void testGet() throws Exception {
            mvc.perform(
                    get(String.format("/developers/%d", developer.getId()))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andExpect(jsonPath("$.id").value(developer.getId()))
                    .andExpect(jsonPath("$.name").value(developer.getName()))
                    .andExpect(jsonPath("$.assigned_bugs").isArray())
                    .andExpect(jsonPath("$.assigned_bugs").value(hasSize(0)))
                    .andExpect(jsonPath("$.assigned_stories").isArray())
                    .andExpect(jsonPath("$.assigned_stories").value(hasSize(0)))
            ;
        }

        @Test
        @DisplayName("should be able to delete it")
        public void testDelete() throws Exception {
            mvc.perform(
                    delete(String.format("/developers/%s", developer.getId()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("should return developer already exists error")
        public void testCreateDuplicated() throws Exception {
            mvc.perform(
                    post("/developers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(String.format("{\"name\":\"%s\"}", developer.getName())))
                    .andExpect(status().isConflict())
                    .andExpect(status().reason("Duplicated name"));
        }

    }
}
