package com.unamur.umatters;

import java.util.ArrayList;
import java.util.List;

public class Box {
    private int id;
    private List<Choice> choices;
    private User creator;
    private String date;
    private ArrayList<String> likes;
    private List<String> tags;
    private String title;
    private String type;

    public Box(int id, List<Choice> choices, User creator, String date, ArrayList<String> likes, List<String> tags, String title, String type) {
        this.id = id;
        this.choices = choices;
        this.creator = creator;
        this.date = date;
        this.likes = likes;
        this.tags = tags;
        this.title = title;
        this.type = type;
    }


    public int getId() {
        return id;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public User getCreator() {
        return creator;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getRole() {
        return this.creator.getRole();
    }

    public String getName() {
        return this.creator.getName();
    }
}
