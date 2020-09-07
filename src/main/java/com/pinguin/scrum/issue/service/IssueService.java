package com.pinguin.scrum.issue.service;

import com.pinguin.scrum.developer.service.DeveloperService;
import com.pinguin.scrum.issue.exception.DuplicatedIssueTitleException;
import com.pinguin.scrum.issue.exception.IssueNotFoundException;
import com.pinguin.scrum.issue.repository.BugRepository;
import com.pinguin.scrum.issue.repository.StoryRepository;
import com.pinguin.scrum.issue.repository.entity.Bug;
import com.pinguin.scrum.issue.repository.entity.Issue;
import com.pinguin.scrum.issue.repository.entity.Story;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Try;

import java.util.Optional;
import java.util.function.Consumer;

public class IssueService {

    private final BugRepository bugRepository;
    private final StoryRepository storyRepository;
    private final DeveloperService developerService;

    public IssueService(BugRepository bugRepository, StoryRepository storyRepository, DeveloperService developerService) {
        this.bugRepository = bugRepository;
        this.storyRepository = storyRepository;
        this.developerService = developerService;
    }

    public Try<Story> createStory(String title, String description, Long estimation) {
        return create(new Story(title, description, estimation), storyRepository::existsByTitle, storyRepository::save);
    }

    public Optional<Story> getStory(Long id) {
        return this.storyRepository.findById(id);
    }

    public List<Story> getAllStories() {
        return List.ofAll(storyRepository.findAll());
    }

    public Try<Story> deleteStory(Long id) {
        return delete(id, storyRepository::findById, storyRepository::delete);
    }

    public List<Bug> getAllBugs() {
        return List.ofAll(bugRepository.findAll());
    }

    public Optional<Bug> getBug(Long id) {
        return this.bugRepository.findById(id);
    }

    public Try<Bug> createBug(String title, String description, Bug.Priority priority) {
        return create(new Bug(title, description, priority), bugRepository::existsByTitle, bugRepository::save);
    }

    public Try<Bug> deleteBug(Long bugId) {
        return delete(bugId, bugRepository::findById, bugRepository::delete);
    }

    private <T extends Issue> Try<T> create(T issue, Function1<String, Boolean> exists, Function1<T, T> save) {
        return Try.of(() -> {
            if (exists.apply(issue.getTitle())) {
                throw new DuplicatedIssueTitleException();
            }

            return save.apply(issue);
        });
    }

    private <T extends Issue> Try<T> delete(Long id, Function1<Long, Optional<T>> finder, Consumer<T> delete) {
        return Try.of(() ->
                    finder.apply(id).orElseThrow(IssueNotFoundException::new)
                ).map(issue -> {
                    delete.accept(issue);
                    return issue;
                });
    }

}
