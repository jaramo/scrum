package com.pinguin.scrum.developer.controller;

import com.pinguin.scrum.developer.dto.DeveloperRequest;
import com.pinguin.scrum.developer.dto.DeveloperResponse;
import com.pinguin.scrum.developer.exception.DeveloperNotFoundException;
import com.pinguin.scrum.developer.service.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;


@RestController
@RequestMapping("/developers")
public class DeveloperController {

    @Autowired
    private DeveloperService developerService;

    @RequestMapping(value = "", method = {RequestMethod.GET})
    @ResponseStatus(HttpStatus.OK)
    public List<DeveloperResponse> getAll() {
        return developerService.getAllDevelopers()
                               .map(DeveloperResponse::fromEntity)
                               .asJava();
    }

    @RequestMapping(value = "/{developerId}", method = {RequestMethod.GET})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public DeveloperResponse get(@PathVariable @NotBlank Long developerId) {
        return developerService.get(developerId)
                .map(DeveloperResponse::fromEntity)
                .orElseThrow(DeveloperNotFoundException::new);
    }

    @RequestMapping(value = "", method = {RequestMethod.POST})
    @ResponseStatus(HttpStatus.CREATED)
    public DeveloperResponse create(@Valid @RequestBody DeveloperRequest dto) {
        return developerService.create(dto.name)
                               .map(DeveloperResponse::fromEntity)
                               .get();
    }

    @RequestMapping(value = "/{developerId}", method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @NotEmpty Long developerId) {
        developerService.delete(developerId).get();
    }
}
