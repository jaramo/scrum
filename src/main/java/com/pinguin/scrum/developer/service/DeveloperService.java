package com.pinguin.scrum.developer.service;

import com.pinguin.scrum.developer.exception.DeveloperNotFoundException;
import com.pinguin.scrum.developer.exception.DuplicatedDeveloperNameException;
import com.pinguin.scrum.developer.repository.DeveloperRepository;
import com.pinguin.scrum.developer.repository.entity.Developer;
import io.vavr.collection.List;
import io.vavr.control.Try;

import java.util.Optional;


public class DeveloperService {

    private final DeveloperRepository repository;

    public DeveloperService(DeveloperRepository repository) {
        this.repository = repository;
    }

    public List<Developer> getAllDevelopers() {
        return List.ofAll(this.repository.findAll());
    }

    public Try<Developer> findById(Long id) {
        return Try .of(() -> repository.findById(id).orElseThrow(DeveloperNotFoundException::new));
    }

    public Optional<Developer> getById(Long id) {
        return this.repository.findById(id);
    }

    public Try<Developer> create(String name) {
        return Try.of(() -> {
            if (repository.existsByName(name)) {
                throw new DuplicatedDeveloperNameException();
            }

            return repository.save(new Developer(name));
        });
    }

    public Try<Developer> delete(Long developerId) {
        return findById(developerId)
                .map(developer -> {
                    repository.delete(developer);
                    return developer;
                });
    }

}
