package com.example.frontend.dto;

public class Lecturer {

    private String id;
    private String name;
    private String email;
    private String password;
    private String department;
    private String contact;
    private String courseAssign;

    // Default constructor
    public Lecturer() {
    }

    // Parameterized constructor
    public Lecturer(String id, String name, String email, String password, String department, String contact, String courseAssign) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.department = department;
        this.contact = contact;
        this.courseAssign = courseAssign;
    }

    // Getters and Setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    @Override
    public String toString() {
        return "Lecturer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", department='" + department + '\'' +
                ", contact='" + contact + '\'' +
                ", courseAssign='" + courseAssign + '\'' +
                '}';
    }
}
