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

public class GetAllComOfBox extends AsyncTask<String, String, String> {

    private CommentListAdapter adapter = null;
    private String id_box;

    public GetAllComOfBox(){
        //set context variables if required
    }

    public GetAllComOfBox(CommentListAdapter adapter, String id_box) {
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
            for(int i=0; i<data.length(); i++) {
                com.unamur.umatters.Comment com = createComFromJson(data.getJSONObject(i));
                if (com!=null){
                    adapter.addData(com);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private com.unamur.umatters.Comment createComFromJson(JSONObject json) {
        Log.d("comment data: ", json.toString());

        com.unamur.umatters.Comment comment = null;

        try{

            //Get comment elements

            String id_box = json.getString("id_box");
            String id_message = json.getString("message_id");
            String content = json.getString("content");

            //Date
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            String date = "";
            try {
                Date dateNF = dateFormat.parse(json.getString("date"));
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                date = formatter.format(dateNF);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            JSONObject object_creator = json.getJSONObject("auteur");
            String email = object_creator.getString("email");
            String firstname = object_creator.getString("firstname");
            String lastname = object_creator.getString("lastname");
            String role = object_creator.getString("role");
            User creator = new User(email, firstname, lastname, role);

            ArrayList<String> likes = new ArrayList<>();
            if (!json.isNull("like")) {
                JSONArray array_likes = json.getJSONArray("like");
                if (array_likes != null) {
                    for (int i = 0; i < array_likes.length(); i++) {
                        likes.add(array_likes.getString(i));
                    }
                }
            }

            ArrayList<Comment> replies = new ArrayList<>();

            GetAllRepliesOfCom getReplies = new GetAllRepliesOfCom(replies, id_box, id_message, adapter);
            getReplies.execute("http://mdl-std01.info.fundp.ac.be/api/v1/messages/repondre");

            //create comment object
            comment = new Comment(id_message, creator, date, content, likes, replies);

            if (!json.isNull("parent")) {
                return null;
            }

        } catch (JSONException e){
            e.printStackTrace();
        }

        return comment;
    }
}
