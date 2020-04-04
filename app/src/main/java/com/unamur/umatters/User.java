package com.unamur.umatters;

public class User {

    private String id;
    private String firstname;
    private String lastname;
    private String role;

    public User() {
        this.id = "1";
        this.firstname = "User";
        this.lastname = "Test";
        this.role = "Etudiant";
    }

    public User(String id, String firstname, String lastname, String role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getName() {
        return (getFirstname() + " " + getLastname());
    }

    public String getRole() {
        return role;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
