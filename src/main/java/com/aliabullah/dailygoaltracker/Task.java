package com.aliabullah.dailygoaltracker;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String taskDate;
    private String dueDate;

    private boolean completed;

    public Task() {
        taskDate = String.valueOf(LocalDate.now(java.time.ZoneId.of("Asia/Karachi")));
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public Category getCategory() {
        return category;
    }

    public String getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    // Setters

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}