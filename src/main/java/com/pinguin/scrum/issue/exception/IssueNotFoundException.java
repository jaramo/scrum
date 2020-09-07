package com.pinguin.scrum.issue.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = IssueNotFoundException.MESSAGE)
public class IssueNotFoundException extends RuntimeException {

    public static final String MESSAGE = "Issue not found";

    public IssueNotFoundException() {
        super(MESSAGE);
    }
}
