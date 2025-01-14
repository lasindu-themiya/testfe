package com.example.frontend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDTO {

    private String id;     // e.g. "E004"
    private String name;   // e.g. "Announcement", "Workshop", "Interview"
    private String date;   // e.g. "2025-01-11T09:50:30.886+00:00"
    private String time;   // e.g. "15:20:31"
    private String status; // e.g. "Assigned"
    private String sId;    // studentId
    private String lId;    // lecturerId
    private String bId;    // batchId

    // Getters & setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getSId() {
        return sId;
    }
    public void setSId(String sId) {
        this.sId = sId;
    }

    public String getLId() {
        return lId;
    }
    public void setLId(String lId) {
        this.lId = lId;
    }

    public String getBId() {
        return bId;
    }
    public void setBId(String bId) {
        this.bId = bId;
    }
}
