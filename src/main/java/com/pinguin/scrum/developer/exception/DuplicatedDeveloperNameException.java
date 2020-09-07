package com.pinguin.scrum.developer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = DuplicatedDeveloperNameException.MESSAGE)
public class DuplicatedDeveloperNameException extends RuntimeException {

    public static final String MESSAGE = "Duplicated name";

    public DuplicatedDeveloperNameException() {
        super(MESSAGE);
    }

}
