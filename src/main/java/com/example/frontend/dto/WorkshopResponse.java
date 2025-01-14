package com.example.frontend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Time;
import java.util.Date;
@JsonIgnoreProperties(ignoreUnknown = true)

public class WorkshopResponse {

    private String eventId;


    public String getwId() {
        return wId;
    }

    public void setwId(String wId) {
        this.wId = wId;
    }

    private String wId;
    private String name;
    private String type;
    private String contact;
    private Date date;
    private Time time;
    private String status;
    private String studentId;
    private String batchId;

    @JsonCreator
    public WorkshopResponse(
            @JsonProperty("eventId")   String eventId,
            @JsonProperty("wId")         String wId,
            @JsonProperty("name")      String name,
            @JsonProperty("type")      String type,
            @JsonProperty("contact")   String contact,
            @JsonProperty("date")      Date date,
            @JsonProperty("time")      Time time,
            @JsonProperty("status")    String status,
            @JsonProperty("studentId") String studentId,
            @JsonProperty("batchId")   String batchId
    ) {
        this.eventId = eventId;
        this.wId = wId;
        this.name = name;
        this.type = type;
        this.contact = contact;
        this.date = date;
        this.time = time;
        this.status = status;
        this.studentId = studentId;
        this.batchId = batchId;
    }

    // getters & setters
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

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
