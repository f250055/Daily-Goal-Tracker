package com.aliabullah.dailygoaltracker;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
@Entity public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private LocalDate taskDate;
    private boolean completed;
    public Task(){
        taskDate = LocalDate.now(java.time.ZoneId.of("Asia/Karachi"));
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public boolean isCompleted(){
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    public void setTaskDate(LocalDate taskDate) {
        this.taskDate = taskDate;
    }
    public LocalDate getTaskDate() {
        return taskDate;
    }

    public String getDescription() {
        return description;
    }
}
