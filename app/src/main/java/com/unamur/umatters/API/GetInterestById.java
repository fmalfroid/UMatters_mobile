package com.unamur.umatters.API;

import android.os.AsyncTask;
import android.util.Log;
import com.unamur.umatters.Box;
import com.unamur.umatters.BoxListAdapter;
import com.unamur.umatters.Choice;
import com.unamur.umatters.Comment;
import com.unamur.umatters.CommentListAdapter;
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

public class GetInterestById extends AsyncTask<String, String, String> {

    private BoxListAdapter adapter = null;
    private String id_box;

    public GetInterestById(){
        //set context variables if required
    }

    public GetInterestById(BoxListAdapter adapter, String id_box) {
        this.adapter = adapter;
        this.id_box = id_box;
    }

    protected void onPreExecute() {
        super.onPreExecute();

    }

    protected String doInBackground(String... params) {


        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            String str_url = params[0] + "/" + id_box;
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
            JSONObject jsonObj = new JSONObject(result);
            JSONArray data = jsonObj.getJSONArray("data");
            adapter.addData(createBoxFromJson(data.getJSONObject(0)));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private List<Choice> createChoiceListFromJson(JSONObject json) {
        ArrayList<Choice> choices = new ArrayList<Choice>();

        Iterator<String> keys = json.keys();

        while(keys.hasNext()) {
            String key = keys.next();
            try {
                JSONArray jArrayUsers = json.getJSONArray(key);
                ArrayList<String> users = new ArrayList<String>();
                if (jArrayUsers != null) {
                    for (int i=0;i<jArrayUsers.length();i++){
                        if (!jArrayUsers.getString(i).equals("")) {
                            users.add(jArrayUsers.getString(i));
                        }
                    }
                    choices.add(new Choice(key, users));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return choices;
    }

    private User createUserFromJson(JSONObject json) {
        String id;
        String firstname;
        String lastname;
        String role;

        try {
            id = json.getString("email");
            firstname = json.getString("firstname");
            lastname = json.getString("lastname");
            role = json.getString("role");

            return new User(id, firstname, lastname, role);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new User();
    }

    private Box createBoxFromJson(JSONObject json) {
        Log.d("data element: ", json.toString());

        String id;
        List<Choice> choices;
        User creator;
        String date;
        ArrayList<String> likes;
        List<String> tags;
        String title;
        String type;
        String description;

        try {
            id = json.getString("id_box");

            //Choices
            if (!json.isNull("choix")) {
                choices = createChoiceListFromJson(json.getJSONObject("choix"));
            } else {
                choices = Arrays.asList(new Choice());
            }

            //Creator
            creator = createUserFromJson(json.getJSONObject("createur"));

            //Date
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date dateNF = dateFormat.parse(json.getString("date_creation"));
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            date = formatter.format(dateNF);

            //likes
            likes = new ArrayList<String>();
            if (!json.isNull("like")) {
                JSONArray jArrayLikes = json.getJSONArray("like");
                if (jArrayLikes != null) {
                    for (int i = 0; i < jArrayLikes.length(); i++) {
                        likes.add(jArrayLikes.getString(i));
                    }
                }
            }

            //Tags
            tags = new ArrayList<String>();
            JSONArray jArrayTags = json.getJSONArray("tag");
            if (jArrayTags != null) {
                for (int i=0;i<jArrayTags.length();i++){
                    tags.add(jArrayTags.getString(i));
                }
            }

            title = json.getString("titre");

            type = json.getString("type");

            description = json.getString("description");

            return new Box(id, choices,creator, date, likes, tags, title, type, description);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
