package com.unamur.umatters;

import java.util.ArrayList;

public class Archive {

    private Box box;
    private ArrayList<String> list_response;
    private ArrayList<String> list_response_date;

    public Archive(){
        this.box = null;
        this.list_response = null;
        this.list_response_date = null;
    }

    public Archive(Box box, ArrayList<String> list_response, ArrayList<String> list_response_date) {
        this.box = box;
        this.list_response = list_response;
        this.list_response_date = list_response_date;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public ArrayList<String> getList_response() {
        return list_response;
    }

    public void setList_response(ArrayList<String> list_response) {
        this.list_response = list_response;
    }

    public ArrayList<String> getList_response_date() {
        return list_response_date;
    }

    public void setList_response_date(ArrayList<String> list_response_date) {
        this.list_response_date = list_response_date;
    }
}
