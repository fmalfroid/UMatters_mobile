package com.unamur.umatters;

public class Notif {

    private String text;
    private String hour;
    private String date;
    private User LinkUser;
    private boolean seen;

    public Notif(String text, String hour, String date, User linkUser, boolean seen) {
        this.text = text;
        this.hour = hour;
        this.date = date;
        this.LinkUser = linkUser;
        this.seen = seen;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getLinkUser() {
        return LinkUser;
    }

    public void setLinkUser(User linkUser) {
        LinkUser = linkUser;
    }
}
