package com.unamur.umatters.API;

import android.os.AsyncTask;

import com.unamur.umatters.CurrentUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SetCurrentUser extends AsyncTask<String, String, String> {

    public SetCurrentUser(){ }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {


        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");

            }

            return buffer.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println(result);
        CurrentUser user = CurrentUser.getCurrentUser();
        if (result == null) {
            System.out.println("An Error Occurred");
        } else {
            try {
                JSONObject jsonResultObj = new JSONObject(result);
                if (jsonResultObj.getBoolean("success")) {
                    JSONObject jsonObj = jsonResultObj.getJSONArray("data").getJSONObject(0);

                    String email = jsonObj.getString("email");
                    String firstname = jsonObj.getString("firstname");
                    String lastname = jsonObj.getString("lastname");
                    String role = jsonObj.getString("role");
                    String faculty = jsonObj.getString("faculte");
                    int participation = jsonObj.getInt("participation");
                    ArrayList<String> box = new ArrayList<>();
                    if (!jsonObj.isNull("box")) {
                        JSONArray jArrayBox = jsonObj.getJSONArray("box");
                        if (jArrayBox != null) {
                            for (int i = 0; i < jArrayBox.length(); i++) {
                                box.add(jArrayBox.getString(i));
                            }
                        }
                    }
                    JSONObject notifications;
                    if (!jsonObj.isNull("notif")) {
                        notifications = jsonObj.getJSONObject("notif");
                    } else {
                        notifications = new JSONObject();
                    }
                    JSONObject sanctions;
                    if (!jsonObj.isNull("sanctions")) {
                        sanctions = jsonObj.getJSONObject("sanctions");
                    } else {
                        sanctions = new JSONObject();
                    }

                    user.setEmail(email);
                    user.setFirstname(firstname);
                    user.setLastname(lastname);
                    user.setRole(role);
                    user.setFaculty(faculty);
                    user.setParticipation(participation);
                    user.setBox(box);
                    user.setNotifications(notifications);
                    user.setSanctions(sanctions);
                    ArrayList<String> interet = new ArrayList<>();
                    if (!jsonObj.isNull("interet")) {
                        JSONArray jArrayInteret = jsonObj.getJSONArray("interet");
                        if (jArrayInteret != null) {
                            for (int i = 0; i < jArrayInteret.length(); i++) {
                                interet.add(jArrayInteret.getString(i));
                            }
                        }
                    }
                    user.setInterest(interet);
                    ArrayList<String> subscriptions = new ArrayList<>();
                    if (!jsonObj.isNull("abonnement")) {
                        JSONArray jArrayAbonnement = jsonObj.getJSONArray("abonnement");
                        if (jArrayAbonnement != null) {
                            for (int i = 0; i < jArrayAbonnement.length(); i++) {
                                subscriptions.add(jArrayAbonnement.getString(i));
                            }
                        }
                    }
                    user.setSubscriptions(subscriptions);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
