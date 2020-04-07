package com.unamur.umatters;

import java.util.ArrayList;
import org.json.JSONObject;

public class CurrentUser {
    static private CurrentUser currentUser = null;

    private String email;
    private String firstname;
    private String lastname;
    private String role;
    private String password;
    private int participation;
    private ArrayList<String> box;
    private JSONObject notifications;
    private JSONObject sanctions;
    private ArrayList<String> interest;

    private CurrentUser() {}

    static public CurrentUser getCurrentUser() {
        if (currentUser == null) {
            currentUser = new CurrentUser();
        }
        return currentUser;
    }

    public void setCurrentUser(String email, String firstname, String lastname, String role, String password, int participation, ArrayList<String> box, JSONObject notifications, JSONObject sanctions) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.password = password;
        this.participation = participation;
        this.box = box;
        this.notifications = notifications;
        this.sanctions = sanctions;
    }

    public String getRole() {
        return role;
    }

    public String getFirstname() {
        return firstname;
    }

    public ArrayList<String> getBox() {
        return box;
    }

    public int getParticipation() {
        return participation;
    }

    public JSONObject getNotifications() {
        return notifications;
    }

    public String getEmail() {
        return email;
    }

    public JSONObject getSanctions() {
        return sanctions;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getInterest() {
        return interest;
    }

    public void setBox(ArrayList<String> box) {
        this.box = box;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setNotifications(JSONObject notif) {
        this.notifications = notif;
    }

    public void setParticipation(int participation) {
        this.participation = participation;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSanctions(JSONObject sanctions) {
        this.sanctions = sanctions;
    }

    public void setInterest(ArrayList<String> interest) {
        this.interest = interest;
    }
}
