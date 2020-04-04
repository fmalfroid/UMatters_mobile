package com.unamur.umatters;

import android.graphics.Bitmap;

public class SubscriptionsPerson {

    private Bitmap image;
    private String email;
    private String firstname;
    private String surname;
    private String faculty;
    private boolean subscribed;
    private String role;
    private int level;

    public SubscriptionsPerson(Bitmap image, String email, String firstname, String surname, String faculty, boolean subscribed, String role, int level) {
        this.image = image;
        this.email = email;
        this.firstname = firstname;
        this.surname = surname;
        this.faculty = faculty;
        this.subscribed = subscribed;
        this.role = role;
        this.level = level;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
}
