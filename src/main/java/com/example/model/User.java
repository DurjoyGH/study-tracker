package com.example.model;

public class User {
    private String firstName;
    private String lastName;
    private String institution;
    private String gender;
    private String userName;
    private String password;

    public User() {
    }

    public User(String firstName, String lastName, String institution, String gender, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.institution = institution;
        this.gender = gender;
        this.userName = userName;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getInstitution() {
        return institution;
    }

    public String getGender() {
        return gender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
