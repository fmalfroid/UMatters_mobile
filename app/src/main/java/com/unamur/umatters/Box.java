package com.unamur.umatters;

import android.util.Pair;

import java.util.List;

public class Box {
    private String type;
    private String name;
    private String date;
    private String role;
    private int nb_likes;
    private List<String> tags;
    private String text;
    private List<Pair<String, Integer>> choices;

    public Box(String type, String name, String date, String role, int nb_likes, List<String> tags, String text, List<Pair<String, Integer>> choices) {
        this.type = type;
        this.name = name;
        this.date = date;
        this.role = role;
        this.nb_likes = nb_likes;
        this.tags = tags;
        this.text = text;
        this.choices = choices;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String getDate() {
        return this.date;
    }

    public String getRole() {
        return this.role;
    }

    public int getNb_likes() {
        return this.nb_likes;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public String getText() {
        return this.text;
    }

    public List<Pair<String, Integer>> getChoices() {
        return this.choices;
    }
}
