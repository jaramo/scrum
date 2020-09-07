package com.pinguin.scrum.issue.repository.entity;

import javax.persistence.*;

@Entity
@Table(name = "bugs")
public class Bug extends Issue {

    public enum Priority {
        MINOR,
        MAJOR,
        CRITICAL,
    }

    public enum Status {
        NEW,
        VERIFIED,
        RESOLVED,
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    public Bug() {
        super();
    }

    public Bug(String title, String description, Priority priority) {
        super(title, description);
        this.priority = priority;
        this.status = Status.NEW;
    }

    public Priority getPriority() {
        return priority;
    }

    public Status getStatus() {
        return status;
    }
}
