package com.example.frontend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkshopDTO {

    private String wId;      // e.g. "W003"
    private String type;     // e.g. "Technical"
    private String contact;  // e.g. "0123456789"

    private EventDTO event;  // the embedded event object

    // Getters & setters
    public String getwId() {
        return wId;
    }
    public void setwId(String wId) {
        this.wId = wId;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }

    public EventDTO getEvent() {
        return event;
    }
    public void setEvent(EventDTO event) {
        this.event = event;
    }
}
