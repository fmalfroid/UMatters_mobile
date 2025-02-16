package com.unamur.umatters.API;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.unamur.umatters.Box;
import com.unamur.umatters.BoxListAdapter;
import com.unamur.umatters.BoxListAdapterProfile;
import com.unamur.umatters.Choice;
import com.unamur.umatters.CurrentUser;
import com.unamur.umatters.R;
import com.unamur.umatters.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
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

public class GetAllBoxProfile extends AsyncTask<String, String, String> {

    private BoxListAdapterProfile adapter = null;
    private Context context = null;


    //public Box testbox1 = new Box("1", (List<Choice>) Arrays.asList(new Choice("3e étage", new ArrayList<String>()), new Choice("4e étage", new ArrayList<String>())), new User("1", "Patrick Heymans", "Académique"), "28-02-2020", new ArrayList<String>(), (List<String>) Arrays.asList("#Info", "#Matériel"), "Où ajouter une machine à café?", "choix_multiple", "");
    //public Box testBox2 = new Box("2", (List<Choice>) Arrays.asList(new Choice("oui", new ArrayList<String>()), new Choice("non", new ArrayList<String>())), new User("2", "Anthony Etienne", "Scientifique"), "28-02-2020", new ArrayList<String>(), (List<String>) Arrays.asList("#Général", "#BUMP", "#Horaire"), "Laisser les BUMP ouverte jusque 18h le vendredi?", "oui_non", "");
    //public Box testBox3 = new Box("3", (List<Choice>) Arrays.asList(new Choice()), new User("1", "Florian Malfroid", "Etudiant"), "28-02-2020", new ArrayList<String>(), (List<String>) Arrays.asList("#Info", "#Matériel"), "Changer les souris du i21", "textuelle", "");
    //public Box testBox4 = new Box("4", (List<Choice>) Arrays.asList(new Choice()), new User("1", "Joséphine AngeGardien", "ATG"), "28-02-2020", new ArrayList<String>(), (List<String>) Arrays.asList("#Général", "#Arsenal"), "Je propose de rajouter du bouillon au poulet avec le riz de jeudi. Vous en pensez quoi?", "textuelle", "");

    public GetAllBoxProfile(){
        //set context variables if required
    }

    public GetAllBoxProfile(BoxListAdapterProfile adapter, Context context) {
        this.adapter = adapter;
        this.context = context;
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


            InputStream stream;

            try {
                stream = connection.getInputStream();
            } catch (FileNotFoundException e) {
                stream = connection.getErrorStream();
            }

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
            CurrentUser current_user = CurrentUser.getCurrentUser();
            JSONObject jsonObj = new JSONObject(result);
            JSONArray data = jsonObj.getJSONArray("data");
            for(int i=0; i<data.length(); i++) {
                //Get all box created by the current user
                Box box = createBoxFromJson(data.getJSONObject(i));
                adapter.addData(box);
            }
            TextView txt_no_sub = ((Activity) context).findViewById(R.id.txt_no_box);
            if (data.length()>0) {
                txt_no_sub.setVisibility(View.GONE);
            } else {
                txt_no_sub.setVisibility(View.VISIBLE);
            }
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

            return new User(id, firstname, lastname, role, null, "", 0);
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
            if (!json.isNull("tag_principale")) {
                tags.add(json.getString("tag_principale"));
            }
            if (!json.isNull("tag_secondaire")) {
                JSONArray jArrayTags = json.getJSONArray("tag_secondaire");
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
