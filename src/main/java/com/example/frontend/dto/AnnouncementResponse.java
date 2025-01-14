package com.example.frontend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Time;
import java.util.Date;
@JsonIgnoreProperties(ignoreUnknown = true)

public class AnnouncementResponse {

    private String eventId;   // e.g. "E004"


    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    private String aId;
    private String name;      // e.g. "Announcement"
    private String type;      // e.g. "General"
    private String content;   // e.g. "Test content"
    private Date date;        // event date
    private Time time;        // event time
    private String status;    // e.g. "Assigned"
    private String studentId; // e.g. "gadse233f-001"
    private String batchId;   // e.g. null or "B001"

    /**
     * The single constructor used for both normal instantiation and Jackson deserialization.
     * Using @JsonCreator + @JsonProperty ensures Jackson knows how to map JSON fields.
     */
    @JsonCreator
    public AnnouncementResponse(
            @JsonProperty("eventId")   String eventId,
            @JsonProperty("aId")   String aId,
            @JsonProperty("name")      String name,
            @JsonProperty("type")      String type,
            @JsonProperty("content")   String content,
            @JsonProperty("date")      Date date,
            @JsonProperty("time")      Time time,
            @JsonProperty("status")    String status,
            @JsonProperty("studentId") String studentId,
            @JsonProperty("batchId")   String batchId
    ) {
        this.eventId   = eventId;
        this.aId = aId;
        this.name      = name;
        this.type      = type;
        this.content   = content;
        this.date      = date;
        this.time      = time;
        this.status    = status;
        this.studentId = studentId;
        this.batchId   = batchId;
    }

    // =========== Getters & Setters ===========

    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }
    public void setTime(Time time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getBatchId() {
        return batchId;
    }
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
}
