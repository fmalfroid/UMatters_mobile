package com.unamur.umatters;

import java.util.ArrayList;
import java.util.List;

public class Choice {

    private String name;
    private ArrayList<String> users;

    public  Choice() {
        this.name = "null";
        this.users = new ArrayList<String>();
    }

    public Choice(String name, ArrayList<String> users) {
        this.name = name;
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getUsers() {
        return users;
    }
}
