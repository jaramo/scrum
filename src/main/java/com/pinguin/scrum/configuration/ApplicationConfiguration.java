package com.pinguin.scrum.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@Configuration
@EnableConfigurationProperties(SprintProperties.class)
@EnableJpaAuditing
public class ApplicationConfiguration {


}
