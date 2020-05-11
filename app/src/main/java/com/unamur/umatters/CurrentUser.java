package com.unamur.umatters;

import android.graphics.Bitmap;

import java.util.ArrayList;
import org.json.JSONObject;

public class CurrentUser {
    static private CurrentUser currentUser = null;

    private String email;
    private String firstname;
    private String lastname;
    private String role;
    private Bitmap image;
    private String faculty;
    private String password;
    private int participation;
    private ArrayList<String> box;
    private ArrayList<Notif> notifications;
    private JSONObject sanctions;
    private ArrayList<String> interest;
    private ArrayList<String> subscriptions;
    private ArrayList<String> tag_pref;
    private int followers;
    private int following;

    private CurrentUser() {}

    static public CurrentUser getCurrentUser() {
        if (currentUser == null) {
            currentUser = new CurrentUser();
        }
        return currentUser;
    }

    public CurrentUser(String email, String firstname, String lastname, String role, Bitmap image, String faculty, String password, int participation, ArrayList<String> box, ArrayList<Notif> notifications, JSONObject sanctions, ArrayList<String> interest, ArrayList<String> subscriptions) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.image = image;
        this.faculty = faculty;
        this.password = password;
        this.participation = participation;
        this.box = box;
        this.notifications = notifications;
        this.sanctions = sanctions;
        this.interest = interest;
        this.subscriptions = subscriptions;
    }

    public ArrayList<String> getTag_pref() {
        return tag_pref;
    }

    public void setTag_pref(ArrayList<String> tag_pref) {
        this.tag_pref = tag_pref;
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

    public ArrayList<Notif> getNotifications() {
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

    public ArrayList<String> getSubscriptions() {
        return subscriptions;
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

    public void setNotifications(ArrayList<Notif> notif) {
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

    public void setSubscriptions(ArrayList<String> subscriptions) {
        this.subscriptions = subscriptions;
    }


}
