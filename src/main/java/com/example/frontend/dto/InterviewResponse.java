package com.example.frontend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Time;
import java.util.Date;
@JsonIgnoreProperties(ignoreUnknown = true)

public class InterviewResponse {

    private String eventId; // Must come from event.getId()


    public String getiId() {
        return iId;
    }

    public void setiId(String iId) {
        this.iId = iId;
    }

    private String iId;
    private String name;         // e.g. "Interview"
    private String companyName;
    private String position;
    private String mode;
    private Date date;
    private Time time;
    private String status;
    private String studentId;
    private String batchId;

    @JsonCreator
    public InterviewResponse(
            @JsonProperty("eventId")      String eventId,
            @JsonProperty("iId")           String iId,
            @JsonProperty("name")         String name,
            @JsonProperty("companyName")  String companyName,
            @JsonProperty("position")     String position,
            @JsonProperty("mode")         String mode,
            @JsonProperty("date")         Date date,
            @JsonProperty("time")         Time time,
            @JsonProperty("status")       String status,
            @JsonProperty("studentId")    String studentId,
            @JsonProperty("batchId")      String batchId
    ) {
        this.eventId = eventId;
        this.iId = iId;
        this.name = name;
        this.companyName = companyName;
        this.position = position;
        this.mode = mode;
        this.date = date;
        this.time = time;
        this.status = status;
        this.studentId = studentId;
        this.batchId = batchId;
    }

    // Getters & setters
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public Time getTime() { return time; }
    public void setTime(Time time) { this.time = time; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }
}
