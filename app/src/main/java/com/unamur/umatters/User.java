package com.unamur.umatters;

public class User {

    private String id;
    private String name;
    private String role;

    public User() {
        this.id = "1";
        this.name = "User test";
        this.role = "Etudiant";
    }

    public User(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}
