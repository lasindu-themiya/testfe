package com.example.frontend.dto;

public class InterviewRequest {
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

    private String companyName;
    private String position;
    private String mode;

    public InterviewRequest() { }

    public InterviewRequest(String companyName, String position, String mode) {
        this.companyName = companyName;
        this.position = position;
        this.mode = mode;
    }

    // Getters and Setters
}
