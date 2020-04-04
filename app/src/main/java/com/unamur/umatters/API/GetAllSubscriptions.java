package com.unamur.umatters.API;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import com.unamur.umatters.Box;
import com.unamur.umatters.BoxListAdapter;
import com.unamur.umatters.Choice;
import com.unamur.umatters.CommentListAdapter;
import com.unamur.umatters.SubscriptionsListAdapter;
import com.unamur.umatters.SubscriptionsPerson;
import com.unamur.umatters.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

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

public class GetAllSubscriptions extends AsyncTask<String, String, String> {

    private SubscriptionsListAdapter adapter = null;

    public GetAllSubscriptions(){
        //set context variables if required
    }

    public GetAllSubscriptions(SubscriptionsListAdapter adapter) {
        this.adapter = adapter;
    }

    protected void onPreExecute() {
        super.onPreExecute();

    }

    protected String doInBackground(String... params) {


        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            String str_url = params[0];
            URL url = new URL(str_url);
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
            Log.d("Subscriptions : ", "Not a single subscriptions");
            JSONObject jsonObj = new JSONObject(result);
            JSONArray data = jsonObj.getJSONArray("data");
            for(int i=0; i<data.length(); i++) {
                SubscriptionsPerson sub = createSubFromJson(data.getJSONObject(i));
                if (sub!= null){
                    adapter.addData(sub);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private SubscriptionsPerson createSubFromJson(JSONObject sub_json) {
        Log.d("Subscriptions : ", sub_json.toString());

        Bitmap picture;
        String email;
        String firstname;
        String lastname;
        String faculty;
        String role;
        int level;

        SubscriptionsPerson sub = null;

        try{

            //TODO get picture and faculty
            picture = null;
            faculty = "";

            email = sub_json.getString("email");
            firstname = sub_json.getString("firstname");
            lastname = sub_json.getString("lastname");
            role = sub_json.getString("role");
            level = sub_json.getInt("participation");

            sub = new SubscriptionsPerson(null, email, firstname, lastname, faculty, true, role, level);

        } catch (JSONException e){
            e.printStackTrace();
        }

        return sub;
    }

}
