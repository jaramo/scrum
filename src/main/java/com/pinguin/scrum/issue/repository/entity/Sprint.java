package com.pinguin.scrum.issue.repository.entity;

import com.pinguin.scrum.developer.repository.entity.Developer;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sprint")
@EntityListeners(AuditingEntityListener.class)
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "week_number", nullable = false)
    private Integer weekNumber;

    @ManyToOne @JoinColumn(name = "assigned_developer", nullable = false)
    private Developer assignedDeveloper;

    @ManyToOne @JoinColumn(name = "story", nullable = false)
    private Story story;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Sprint() {}

    public Sprint(Integer weekNumber, Developer assignedDeveloper, Story story) {
        this.weekNumber = weekNumber;
        this.assignedDeveloper = assignedDeveloper;
        this.story = story;
    }

    public Long getId() {
        return id;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public Developer getAssignedDeveloper() {
        return assignedDeveloper;
    }

    public Story getStory() {
        return story;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
