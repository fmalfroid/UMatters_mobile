package com.unamur.umatters.API;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.unamur.umatters.Box;
import com.unamur.umatters.BoxListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class GetAllBox extends AsyncTask<String, String, String> {

    private BoxListAdapter adapter = null;

    //Exemple de choix de sondage
    public List<Pair<String, Integer>> choixTmp = Arrays.asList(
        Pair.create("3e étage", 10),
        Pair.create("4e étage", 7)
    );

    //Exemple de choix oui/non
    private final List<Pair<String, Integer>> choixOuiNon = Arrays.asList(
        Pair.create("yes", 85),
        Pair.create("no", 15)
    );

    public Box testbox1 = new Box("poll", "Patrick Heymans", "28-02-2020", "Academique", 60, (List<String>) Arrays.asList("#Computer Science", "#Matériel"), "Où ajouter une machine à café?", choixTmp);
    public Box testBox2 = new Box("yes_no", "Anonyme", "28-02-2020", "Recteur", 23, (List<String>) Arrays.asList("#General", "#BUMP", "#Horaire"), "Laisser la BUMP ouverte jusque 18h le vendredi?", choixOuiNon);
    public Box testBox3 = new Box("text", "Florian Malfroid", "28-02-2020", "Etudiant", 42, (List<String>) Arrays.asList("#Computer Science", "#Matériel"), "Changer les souris du i21", null);
    public Box testBox4 = new Box("text", "Joséphine AngeGardien", "27-02-2020", "Personnel", 56, (List<String>) Arrays.asList("#General", "#Arsenal"), "Je propose de rajouter du bouillon au poulet avec le riz de jeudi. Vous en pensez quoi?", null);

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
        adapter.addData(testbox1);
        adapter.addData(testBox2);
        adapter.addData(testBox3);
        adapter.addData(testBox4);
        System.out.println(adapter.getItemCount());
        try {
            Log.d("Response: ", result);
            JSONObject jsonObj = new JSONObject(result);  // crée le json
            Log.d("Success: ", String.valueOf(jsonObj.getBoolean("succes")));
            Log.d("Count: ", String.valueOf(jsonObj.getInt("count")));
            Log.d("Data: ", jsonObj.getJSONArray("data").toString()); // récupère un champ du json
            // TODO : Pour toutes les box dans data: créer la box et l'ajouter à l'adapter
        } catch (JSONException e) {
            e.printStackTrace(); // Si la transformation du result en json n'a pas fonctionné
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}
