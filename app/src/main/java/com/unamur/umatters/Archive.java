package com.unamur.umatters;

import java.util.ArrayList;

public class Archive {

    private Box box;
    private ArrayList<Response> responses;

    public Archive() {
        this.box = null;
        this.responses = null;
    }

    public Archive(Box box, ArrayList<Response> responses) {
        this.box = box;
        this.responses = responses;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public ArrayList<Response> getResponses() {
        return responses;
    }

    public void setResponses(ArrayList<Response> responses) {
        this.responses = responses;
    }
}
