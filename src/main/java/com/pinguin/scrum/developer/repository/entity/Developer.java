package com.pinguin.scrum.developer.repository.entity;

import com.pinguin.scrum.issue.repository.entity.Bug;
import com.pinguin.scrum.issue.repository.entity.Issue;
import com.pinguin.scrum.issue.repository.entity.Story;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "developers")
@EntityListeners(AuditingEntityListener.class)
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "assignedDeveloper")
    private List<Story> assignedStories = Collections.emptyList();

    @OneToMany(mappedBy = "assignedDeveloper")
    private List<Bug> assignedBugs = Collections.emptyList();

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Developer() { }

    public Developer(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Story> getAssignedStories() {
        return assignedStories;
    }

    public List<Bug> getAssignedBugs() {
        return assignedBugs;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
