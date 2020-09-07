package com.pinguin.scrum.issue.repository.entity;

import javax.persistence.*;

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

    public Story() {}

    public Story(String title, String description, Long estimation) {
        super(title, description);
        this.estimation = estimation;
        this.status = Status.NEW;
    }

    public Long getEstimation() {
        return estimation;
    }

    public void setEstimation(Long estimation) {
        this.estimation = estimation;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
