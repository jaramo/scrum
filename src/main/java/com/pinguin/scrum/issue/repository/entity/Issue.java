package com.pinguin.scrum.issue.repository.entity;


import com.pinguin.scrum.developer.repository.entity.Developer;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "title", unique = true)
    protected String title;

    @Column(name = "description")
    protected String description;

    @ManyToOne
    @JoinColumn(name = "developer_id", nullable = true)
    protected Developer assignedDeveloper;

    @CreatedDate
    @Column(name = "created_at")
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    protected Issue() {}

    protected Issue(String title, String description) {
        this.title = title;
        this.description = description;
        this.assignedDeveloper = null;
    }

    protected Issue(
        Long id,
        String title,
        String description,
        Developer assignedDeveloper,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.assignedDeveloper = assignedDeveloper;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Developer getAssignedDeveloper() {
        return assignedDeveloper;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public abstract <T extends Issue> T copy(Developer developer);
}
