package com.unamur.umatters.API;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import com.unamur.umatters.Box;
import com.unamur.umatters.BoxListAdapter;
import com.unamur.umatters.BoxListAdapterProfile;
import com.unamur.umatters.BoxListAdapterUsersProfile;
import com.unamur.umatters.Choice;
import com.unamur.umatters.CurrentUser;
import com.unamur.umatters.User;
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
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class GetUser extends AsyncTask<String, String, String> {

    private Context context;
    private BoxListAdapterUsersProfile adapter;

    public GetUser(){
        //set context variables if required
    }

    public GetUser(Context context, BoxListAdapterUsersProfile adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    protected void onPreExecute() {
        super.onPreExecute();

    }

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
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray data = jsonObj.getJSONArray("data");
            JSONObject user = data.getJSONObject(0);

            //get user info into current user
            String email = user.getString("email");
            String firstname = user.getString("firstname");
            String lastname = user.getString("lastname");
            String role = user.getString("role");
            int participation = user.getInt("participation");
            int followers = user.getInt("followers");
            int following = user.getInt("following");

            String str_image = user.getString("image");
            Bitmap image = StringToBitMap(str_image);

            String faculty = user.getString("faculte");

            User userProfile = new User(email, firstname, lastname, role, image, faculty, participation);
            userProfile.setFollowers(followers);
            userProfile.setFollowing(following);

            adapter.setUser(userProfile);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
