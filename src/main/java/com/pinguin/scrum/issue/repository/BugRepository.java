package com.pinguin.scrum.issue.repository;

import com.pinguin.scrum.issue.repository.entity.Bug;
import org.springframework.data.repository.CrudRepository;

public interface BugRepository extends CrudRepository<Bug, Long> {

    boolean existsByTitle(String title);

}