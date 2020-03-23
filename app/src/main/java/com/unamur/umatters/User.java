package com.unamur.umatters;

public class User {

    private String id;
    private String firstname;
    private String role;

    public User(String id, String firstname, String role) {
        this.id = id;
        this.firstname = firstname;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return firstname;
    }

    public String getRole() {
        return role;
    }
}
