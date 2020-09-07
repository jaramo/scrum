package com.pinguin.scrum.developer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = DeveloperNotFoundException.MESSAGE)
public class DeveloperNotFoundException extends RuntimeException {

    public static final String MESSAGE = "Developer not found";

    public DeveloperNotFoundException() {
        super(MESSAGE);
    }

}
