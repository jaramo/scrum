package com.pinguin.scrum.developer.repository;

import com.pinguin.scrum.developer.repository.entity.Developer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DeveloperRepository extends CrudRepository<Developer, java.lang.Long> {

    boolean existsByName(String name);

    Optional<Developer> findByName(String name);

}
