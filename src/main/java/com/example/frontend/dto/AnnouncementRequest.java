package com.example.frontend.dto;

public class AnnouncementRequest {
    private String content;

    public AnnouncementRequest() { }

    public AnnouncementRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
