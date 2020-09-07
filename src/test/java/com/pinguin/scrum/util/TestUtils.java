package com.pinguin.scrum.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper()
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
                    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    .setSerializationInclusion(JsonInclude.Include.ALWAYS)
        ;
    }

    public static String loadFile(String fileName) {
        try {
            URL resource = TestUtils.class.getClassLoader().getResource("fixtures/" + fileName);
            return new String(Files.readAllBytes(Paths.get(resource.toURI())));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJson(Object data) throws JsonProcessingException {
        return MAPPER.writeValueAsString(data);
    }

    public static <T> T getResponse(MvcResult result, Class<T> clazz) throws IOException {
        return MAPPER.readValue(result.getResponse().getContentAsByteArray(), clazz);
    }
}