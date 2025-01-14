package com.example.frontend.dto;

import lombok.Data;

@Data
public class TeamResponse {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
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

    private String id; // Team ID
    private String leaderId; // Leader ID
    private String members; // Comma-separated member IDs
    private String description; // Team description
    private String batchId; // Batch ID
    private String lecturerId; // Lecturer ID

    public TeamResponse(String id, String leaderId, String members, String description, String batchId, String lecturerId) {
        this.id = id;
        this.leaderId = leaderId;
        this.members = members;
        this.description = description;
        this.batchId = batchId;
        this.lecturerId = lecturerId;
    }
}
