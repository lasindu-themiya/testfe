package com.example.frontend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnnouncementDTO {

    private String aId;      // e.g. "AN004" (Announcement ID)
    private String type;     // e.g. "General"
    private String content;  // e.g. "aaaaaaaa"

    // The embedded event object
    private EventDTO event;  // e.g. event.id="E004", event.name="Announcement"

    // Getters & setters

    public String getaId() {
        return aId;
    }
    public void setaId(String aId) {
        this.aId = aId;
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

    public EventDTO getEvent() {
        return event;
    }
    public void setEvent(EventDTO event) {
        this.event = event;
    }
}
