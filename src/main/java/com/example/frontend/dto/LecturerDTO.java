package com.example.frontend.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class LecturerDTO {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCourseAssign() {
        return courseAssign;
    }

    public void setCourseAssign(String courseAssign) {
        this.courseAssign = courseAssign;
    }

    private String id;
    private String password;
    private String name;
    private String email;
    private String department;
    private String contact;
    private String courseAssign;

    public void setPassword(String password) {
        this.password = password;
    }
}
