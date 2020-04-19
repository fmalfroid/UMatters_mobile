package com.unamur.umatters;

import android.graphics.Bitmap;

import java.io.Serializable;

public class User implements Serializable {

    private String id;
    private String firstname;
    private String lastname;
    private String role;
    private Bitmap image;
    private String faculty;
    private int participation;
    private int followers;
    private int following;

    public User() {
        this.id = "";
        this.firstname = "";
        this.lastname = "";
        this.role = "";
        this.image = null;
        this.faculty = "";
        this.participation = 0;
    }

    public User(String id, String firstname, String lastname, String role, Bitmap image, String faculty, int participation) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.image = image;
        this.faculty = faculty;
        this.participation = participation;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public int getParticipation() {
        return participation;
    }

    public void setParticipation(int participation) {
        this.participation = participation;
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
