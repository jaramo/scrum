package com.pinguin.scrum.issue.repository;

import com.pinguin.scrum.issue.repository.entity.Story;
import org.springframework.data.repository.CrudRepository;

public interface StoryRepository extends CrudRepository<Story, Long> {

    boolean existsByTitle(String title);

}