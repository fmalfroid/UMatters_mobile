package com.unamur.umatters;

import java.util.ArrayList;

public class Comment {
    private String id;
    private User creator;
    private String date;
    private String text;
    private ArrayList<String> likes;
    private ArrayList<String> replies;

    public Comment() {
        this.id = "1";
        this.creator = new User();
        this.date = "27-03-2020";
        this.text = "Commentaire test";
        this.likes = new ArrayList<>();
        this.replies = new ArrayList<>();
    }

    public Comment(String id, User creator, String date, String text, ArrayList<String> likes, ArrayList<String> replies) {
        this.id = id;
        this.creator = creator;
        this.date = date;
        this.text = text;
        this.likes = likes;
        this.replies = replies;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public User getCreator() {
        return creator;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public String getText() {
        return text;
    }

    public ArrayList<String> getReplies() {
        return replies;
    }
}
