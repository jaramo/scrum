package com.pinguin.scrum.issue.repository;

import com.pinguin.scrum.issue.repository.entity.Sprint;
import org.springframework.data.repository.CrudRepository;

public interface SprintRepository extends CrudRepository<Sprint, Long> {
}