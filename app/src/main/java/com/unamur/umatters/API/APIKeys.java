package com.unamur.umatters.API;

public class APIKeys {

    private static String WebUrl = "http://mdl-std01.info.fundp.ac.be/";
    private static String APIURL = "http://mdl-std01.info.fundp.ac.be/api/v1/";

    public static String getUrl() {
        return APIURL;
    }

    public static String getWebUrl() {
        return WebUrl;
    }
}
