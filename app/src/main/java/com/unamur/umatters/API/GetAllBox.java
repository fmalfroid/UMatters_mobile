package com.unamur.umatters.API;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.unamur.umatters.Box;
import com.unamur.umatters.BoxListAdapter;
import com.unamur.umatters.Choice;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetAllBox extends AsyncTask<String, String, String> {

    private BoxListAdapter adapter = null;


    public Box testbox1 = new Box(1, (List<Choice>) Arrays.asList(new Choice("3e étage", new ArrayList<User>()), new Choice("4e étage", new ArrayList<User>())), new User("1", "Patrick Heymans", "Académique"), "28-02-2020", new ArrayList<User>(), (List<String>) Arrays.asList("#Computer Science", "#Matériel"), "Où ajouter une machine à café?", "poll");
    public Box testBox2 = new Box(2, (List<Choice>) Arrays.asList(new Choice("yes", new ArrayList<User>()), new Choice("no", new ArrayList<User>())), new User("2", "Anthony Etienne", "Etudiant"), "28-02-2020", new ArrayList<User>(), (List<String>) Arrays.asList("#General", "#BUMP", "#Horaire"), "Laisser les BUMP ouverte jusque 18h le vendredi?", "yes_no");
    public Box testBox3 = new Box(3, (List<Choice>) Arrays.asList(new Choice()), new User("1", "Florian Malfroid", "Etudiant"), "28-02-2020", new ArrayList<User>(), (List<String>) Arrays.asList("#Computer Science", "#Matériel"), "Changer les souris du i21", "text");
    public Box testBox4 = new Box(4, (List<Choice>) Arrays.asList(new Choice()), new User("1", "Joséphine AngeGardien", "ATG"), "28-02-2020", new ArrayList<User>(), (List<String>) Arrays.asList("#General", "#Arsenal"), "Je propose de rajouter du bouillon au poulet avec le riz de jeudi. Vous en pensez quoi?", "text");

    public GetAllBox(){
        //set context variables if required
    }

    public GetAllBox(BoxListAdapter adapter) {
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
        /*adapter.addData(testBox2);
        adapter.addData(testBox3);
        adapter.addData(testBox4);*/
        try {
            JSONObject jsonObj = new JSONObject(result);  // crée le json
            Log.d("Success: ", String.valueOf(jsonObj.getBoolean("succes")));
            Log.d("Count: ", String.valueOf(jsonObj.getInt("count")));
            Log.d("Data: ", jsonObj.getJSONArray("data").toString()); // récupère un champ du json
            // TODO : Pour toutes les box dans data: créer la box et l'ajouter à l'adapter
            JSONArray data = jsonObj.getJSONArray("data");
            for(int i=0; i<data.length(); i++) {
                adapter.addData(createBoxFromJson(data.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace(); // Si la transformation du result en json n'a pas fonctionné
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private List<Choice> createChoiceListFromJson(JSONObject json) {
        ArrayList<Choice> choices = new ArrayList<Choice>();
        choices.add(new Choice());

        return Arrays.asList(new Choice());
    }

    private User createUserFromJson(JSONObject json) {
        String id;
        String name;
        String role;

        try {
            id = json.getString("id_user");
            name = json.getString("firstname");
            role = json.getString("role");

            return new User(id, name, role);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new User();
    }

    private Box createBoxFromJson(JSONObject json) {
        Log.d("data element: ", json.toString());

        int id;
        List<Choice> choices;
        User creator;
        String date;
        ArrayList<User> likes;
        List<String> tags;
        String title;
        String type;

        try {
            id = json.getInt("id_box");

            //Choices
            if (!json.isNull("choix")) {
                choices = createChoiceListFromJson(json.getJSONObject("choix"));
            } else {
                choices = Arrays.asList(new Choice());
            }

            //Creator
            creator = createUserFromJson(json.getJSONObject("creator"));

            date = json.getString("date_cration");

            //likes
            likes = new ArrayList<User>();
            JSONArray jArrayLikes = json.getJSONArray("like");
            if (jArrayLikes != null) {
                for (int i=0;i<jArrayLikes.length();i++){
                    likes.add(createUserFromJson(jArrayLikes.getJSONObject(i)));
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

            return new Box(id, choices,creator, date, likes, tags, title, type);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
