package com.pinguin.scrum.issue.repository.entity;

import com.pinguin.scrum.developer.repository.entity.Developer;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    private Bug(
            Long id,
            String title,
            String description,
            Developer assignedDeveloper,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Priority priority,
            Status status
    ) {
        super(id, title, description, assignedDeveloper, createdAt, updatedAt);
        this.priority = priority;
        this.status = status;
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

    @Override
    public Bug copy(Developer developer) {
        return new Bug(
            this.id,
            this.title,
            this.description,
            developer,
            this.createdAt,
            this.updatedAt,
            this.priority,
            this.status);
    }
}
