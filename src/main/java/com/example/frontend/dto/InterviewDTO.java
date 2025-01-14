package com.example.frontend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InterviewDTO {

    private String iId;         // e.g. "I003"
    private String companyName; // e.g. "ACME Corp"
    private String position;    // e.g. "Developer"
    private String mode;        // e.g. "Online"

    private EventDTO event;     // embedded event

    // Getters & setters
    public String getiId() {
        return iId;
    }
    public void setiId(String iId) {
        this.iId = iId;
    }

    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }

    public String getMode() {
        return mode;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }

    public EventDTO getEvent() {
        return event;
    }
    public void setEvent(EventDTO event) {
        this.event = event;
    }
}
