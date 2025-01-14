package com.example.frontend.dto;

public class WorkshopRequest {
    private String type;
    private String contact;

    public WorkshopRequest() { }

    public WorkshopRequest(String type, String contact) {
        this.type = type;
        this.contact = contact;
    }

    public String getType() {
        return type;
    }
    public String getContact() {
        return contact;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
}
