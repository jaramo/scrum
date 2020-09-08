package com.pinguin.scrum.issue.repository.entity;

import com.pinguin.scrum.developer.repository.entity.Developer;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stories")
public class Story extends Issue {

    public enum Status {
        NEW,
        ESTIMATED,
        COMPLETED,
    }

    @Column(name = "estimation", nullable = false)
    private Long estimation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    public Story() {
        super();
    }

    private Story(
            Long id,
            String title,
            String description,
            Developer assignedDeveloper,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Long estimation,
            Status status
    ) {
        super(id, title, description, assignedDeveloper, createdAt, updatedAt);
        this.estimation = estimation;
        this.status = status;
    }

    public Story(String title, String description, Long estimation) {
        super(title, description);
        this.estimation = estimation;
        this.status = Status.NEW;
    }

    public Long getEstimation() {
        return estimation;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public Story copy(Developer developer) {
        return new Story(
                this.id,
                this.title,
                this.description,
                developer,
                this.createdAt,
                this.updatedAt,
                this.estimation,
                this.status);
    }
}
