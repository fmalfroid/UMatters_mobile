package com.unamur.umatters;

import android.graphics.Bitmap;

public class SubscriptionsPerson {

    private Bitmap image;
    private String firstname;
    private String surname;
    private String faculty;
    private boolean subscribed;

    public SubscriptionsPerson(Bitmap image, String firstname, String surname, String faculty, boolean subscribed) {
        this.image = image;
        this.firstname = firstname;
        this.surname = surname;
        this.faculty = faculty;
        this.subscribed = subscribed;
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
