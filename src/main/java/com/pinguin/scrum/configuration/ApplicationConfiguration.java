package com.pinguin.scrum.configuration;

import com.pinguin.scrum.developer.repository.DeveloperRepository;
import com.pinguin.scrum.developer.service.DeveloperService;
import com.pinguin.scrum.issue.repository.BugRepository;
import com.pinguin.scrum.issue.repository.SprintRepository;
import com.pinguin.scrum.issue.repository.StoryRepository;
import com.pinguin.scrum.issue.service.IssueService;
import com.pinguin.scrum.issue.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@Configuration
@EnableConfigurationProperties(SprintProperties.class)
@EnableJpaAuditing
public class ApplicationConfiguration {

   @Bean
   @Autowired
   public DeveloperService developerService(DeveloperRepository repository) {
      return new DeveloperService(repository);
   }

   @Bean
   @Autowired
   public IssueService issueService(BugRepository bugRepository, StoryRepository storyRepository) {
      return new IssueService(bugRepository, storyRepository);
   }

   @Bean
   @Autowired
   public SprintService sprintService(
        SprintRepository sprintRepository,
        DeveloperService developerService,
        IssueService issueService,
        SprintProperties properties
   ) {
      return new SprintService(
                     sprintRepository,
                     developerService,
                     issueService,
                     properties.getDeveloperCapacityPerWeek());
   }

}
