package com.example.frontend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)

public class FeedbackDTO {

    @JsonProperty("fId") // Maps the JSON field "fId" to this Java field
    private String fid;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(String lecturerId) {
        this.lecturerId = lecturerId;
    }

    private String content;
    private int points;
    private String date;
    private String studentId;
    private String batchId;
    private String lecturerId;

    // Getters and Setters

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    // ... other getters and setters ...

    @Override
    public String toString() {
        return "FeedbackDTO{" +
                "fid='" + fid + '\'' +
                ", content='" + content + '\'' +
                ", points=" + points +
                ", date='" + date + '\'' +
                ", studentId='" + studentId + '\'' +
                ", batchId='" + batchId + '\'' +
                ", lecturerId='" + lecturerId + '\'' +
                '}';
    }
}
