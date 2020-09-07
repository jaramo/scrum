package com.pinguin.scrum.issue.repository.entity;


import com.pinguin.scrum.developer.repository.entity.Developer;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
abstract public class Issue {

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

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Developer getAssignedDeveloper() {
        return assignedDeveloper;
    }

    public void setAssignedDeveloper(Developer assignedDeveloper) {
        this.assignedDeveloper = assignedDeveloper;
    }
}
