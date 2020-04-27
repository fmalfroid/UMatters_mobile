package com.unamur.umatters;

public class Response {

    private String texte;
    private String date;
    private String status;

    public Response() {
        this.texte = "";
        this.date = "";
        this.status = "";
    }

    public Response(String texte, String date, String status) {
        this.texte = texte;
        this.date = date;
        this.status = status;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
