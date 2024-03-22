package com.example.model;

import java.time.LocalDate;

public class Task {

    private Long taskID;
    private Long userID;
    private String task;
    private LocalDate createdDate;
    private String description;

    public Task() {
    }

    public Task(String task, LocalDate createdDate, String description) {
        this.task = task;
        this.createdDate = createdDate;
        this.description = description;
    }

    public Long getTaskID() {
        return taskID;
    }

    public void setTaskID(Long taskID) {
        this.taskID = taskID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
