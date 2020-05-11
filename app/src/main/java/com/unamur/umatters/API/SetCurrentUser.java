package com.unamur.umatters.API;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.unamur.umatters.CurrentUser;
import com.unamur.umatters.MainActivity;
import com.unamur.umatters.Notif;
import com.unamur.umatters.TagsSetupActivity;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SetCurrentUser extends AsyncTask<String, String, String> {

    private Context context = null;

    public SetCurrentUser(Context context){
        this.context = context;
    }

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
                    int followers = jsonObj.getInt("followers");
                    int following = jsonObj.getInt("following");

                    ArrayList<String> box = new ArrayList<>();
                    if (!jsonObj.isNull("box")) {
                        JSONArray jArrayBox = jsonObj.getJSONArray("box");
                        if (jArrayBox != null) {
                            for (int i = 0; i < jArrayBox.length(); i++) {
                                box.add(jArrayBox.getString(i));
                            }
                        }
                    }

                    //Date
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

                    ArrayList<Notif> notifications = new ArrayList<>();
                    if (!jsonObj.isNull("notifs")) {
                        JSONArray jArrayNotif = jsonObj.getJSONArray("notifs");
                        for (int i=0; i<jArrayNotif.length(); i++) {
                            JSONObject notif = jArrayNotif.getJSONObject(i);
                            String text = notif.getString("info");
                            Date dateNF = dateFormat.parse(notif.getString("date"));
                            DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
                            DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
                            String date = dateFormatter.format(dateNF);
                            String time = timeFormatter.format(dateNF);
                            boolean seen = notif.getBoolean("vu");
                            Notif notification = new Notif(text, time, date, null, seen);
                            notifications.add(notification);
                        }
                    }

                    JSONObject sanctions;
                    if (!jsonObj.isNull("sanctions")) {
                        sanctions = jsonObj.getJSONObject("sanctions");
                    } else {
                        sanctions = new JSONObject();
                    }

                    ArrayList<String> interet = new ArrayList<>();
                    if (!jsonObj.isNull("interet")) {
                        JSONArray jArrayInteret = jsonObj.getJSONArray("interet");
                        if (jArrayInteret != null) {
                            for (int i = 0; i < jArrayInteret.length(); i++) {
                                interet.add(jArrayInteret.getString(i));
                            }
                        }
                    }

                    ArrayList<String> subscriptions = new ArrayList<>();
                    if (!jsonObj.isNull("abonnement")) {
                        JSONArray jArrayAbonnement = jsonObj.getJSONArray("abonnement");
                        if (jArrayAbonnement != null) {
                            for (int i = 0; i < jArrayAbonnement.length(); i++) {
                                subscriptions.add(jArrayAbonnement.getString(i));
                            }
                        }
                    }

                    ArrayList<String> tag_pref;
                    if (!jsonObj.isNull("tag_pref")) {
                        tag_pref = new ArrayList<>();
                        JSONArray jArrayPrefTags = jsonObj.getJSONArray("tag_pref");
                        if (jArrayPrefTags != null) {
                            for (int i = 0; i < jArrayPrefTags.length(); i++) {
                                tag_pref.add(jArrayPrefTags.getString(i));
                            }
                        }
                    } else {
                        tag_pref = null;
                    }

                    String str_image = jsonObj.getString("image");
                    Bitmap image = StringToBitMap(str_image);

                    user.setEmail(email);
                    user.setFirstname(firstname);
                    user.setLastname(lastname);
                    user.setRole(role);
                    user.setFaculty(faculty);
                    user.setParticipation(participation);
                    user.setBox(box);
                    user.setNotifications(notifications);
                    user.setSanctions(sanctions);
                    user.setInterest(interet);
                    user.setSubscriptions(subscriptions);
                    user.setFollowers(followers);
                    user.setFollowing(following);
                    user.setImage(image);
                    user.setTag_pref(tag_pref);

                    if (tag_pref == null) {
                        Intent runApp = new Intent(context, TagsSetupActivity.class);
                        context.startActivity(runApp);
                        ((Activity) context).finish();
                    } else {
                        Intent runApp = new Intent(context, MainActivity.class);
                        context.startActivity(runApp);
                        ((Activity) context).finish();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
