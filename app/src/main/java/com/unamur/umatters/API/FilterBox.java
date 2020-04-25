package com.unamur.umatters.API;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.unamur.umatters.Box;
import com.unamur.umatters.BoxListAdapter;
import com.unamur.umatters.Choice;
import com.unamur.umatters.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
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

public class FilterBox extends AsyncTask<String, String, String> {

    private Context context;
    private BoxListAdapter adapter = null;

    public FilterBox(){
        //set context variables if required
    }

    public FilterBox(Context context, BoxListAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String JsonResponse = null;
        String JsonDATA = params[1];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            // is output buffer writter
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            //set headers and method
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(JsonDATA);
            // json data
            writer.close();

            InputStream inputStream;

            try {
                inputStream = urlConnection.getInputStream();
            } catch (FileNotFoundException e) {
                inputStream = urlConnection.getErrorStream();
            }

            //input stream
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine + "\n");
            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            JsonResponse = buffer.toString();
            //response data
            Log.i("Response",JsonResponse);
            try {
                //send to post execute
                return JsonResponse;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;


        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final Exception e) {
                    Log.e("Error", "Error closing stream", e);
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray data = jsonObj.getJSONArray("data");
            for(int i=0; i<data.length(); i++) {
                adapter.addData(createBoxFromJson(data.getJSONObject(i)));
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
