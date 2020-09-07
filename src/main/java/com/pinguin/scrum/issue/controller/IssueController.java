package com.pinguin.scrum.issue.controller;

import com.pinguin.scrum.issue.dto.BugRequest;
import com.pinguin.scrum.issue.dto.BugResponse;
import com.pinguin.scrum.issue.dto.StoryRequest;
import com.pinguin.scrum.issue.dto.StoryResponse;
import com.pinguin.scrum.issue.exception.IssueNotFoundException;
import com.pinguin.scrum.issue.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/issues")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @RequestMapping(value = "/stories", method = {RequestMethod.GET})
    @ResponseStatus(HttpStatus.OK)
    public List<StoryResponse> getAllStories() {
        return issueService.getAllStories()
                           .map(StoryResponse::fromEntity)
                           .asJava();
    }

    @RequestMapping(value = "/stories/{id}", method = {RequestMethod.GET})
    @ResponseStatus(HttpStatus.OK)
    public StoryResponse getStory(@PathVariable Long id) {
        return issueService.getStory(id)
                           .map(StoryResponse::fromEntity)
                           .orElseThrow(IssueNotFoundException::new);
    }

    @RequestMapping(value = "/stories", method = {RequestMethod.POST})
    @ResponseStatus(HttpStatus.CREATED)
    public StoryResponse createStory(@Valid @RequestBody StoryRequest dto) {
        return this.issueService.createStory(dto.title, dto.description, dto.estimation)
                                .map(StoryResponse::fromEntity)
                                .get();
    }

    @RequestMapping(value = "/stories/{id}", method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStory(@PathVariable Long id) {
        issueService.deleteStory(id).get();
    }

    @RequestMapping(value = "/bugs", method = {RequestMethod.GET})
    @ResponseStatus(HttpStatus.OK)
    public List<BugResponse> getAllBugs() {
        return issueService.getAllBugs()
                           .map(BugResponse::fromEntity)
                           .asJava();
    }

    @RequestMapping(value = "/bugs/{id}", method = {RequestMethod.GET})
    @ResponseStatus(HttpStatus.OK)
    public BugResponse getBug(@PathVariable Long id) {
        return issueService.getBug(id)
                           .map(BugResponse::fromEntity)
                           .orElseThrow(IssueNotFoundException::new);
    }

    @RequestMapping(value = "/bugs", method = {RequestMethod.POST})
    @ResponseStatus(HttpStatus.CREATED)
    public BugResponse createBug(@Valid @RequestBody BugRequest dto) {
        return this.issueService.createBug(dto.title, dto.description, dto.priority)
                                .map(BugResponse::fromEntity)
                                .get();
    }

    @RequestMapping(value = "/bugs/{id}", method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBug(@PathVariable Long id) {
        issueService.deleteBug(id).get();
    }

}
