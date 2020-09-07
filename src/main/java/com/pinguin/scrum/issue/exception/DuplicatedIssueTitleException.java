package com.pinguin.scrum.issue.exception;

import com.pinguin.scrum.developer.exception.DuplicatedDeveloperNameException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = DuplicatedIssueTitleException.MESSAGE)
public class DuplicatedIssueTitleException extends RuntimeException {

    public static final String MESSAGE = "Duplicated title";

    public DuplicatedIssueTitleException() {
        super(MESSAGE);
    }
}
