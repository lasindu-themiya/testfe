package com.example.frontend.dto;

import lombok.Data;

@Data
public class TeamRequest {


    public TeamRequest(String members, String lecturerId, String description) {
        this.members = members;
        this.lecturerId = lecturerId;
        this.description = description;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(String lecturerId) {
        this.lecturerId = lecturerId;
    }

    private String members; // Comma-separated member IDs
    private String description;
    private String lecturerId; // Supervising lecturer's ID
}
