package com.unamur.umatters;

import java.util.ArrayList;
import java.util.List;

public class Choice {

    private String name;
    private ArrayList<User> users;

    public  Choice() {
        this.name = "null";
        this.users = new ArrayList<User>();
    }

    public Choice(String name, ArrayList<User> users) {
        this.name = name;
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
