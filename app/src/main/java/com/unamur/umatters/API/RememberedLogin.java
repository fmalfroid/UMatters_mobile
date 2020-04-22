package com.unamur.umatters.API;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.unamur.umatters.CurrentUser;
import com.unamur.umatters.LoginActivity;
import com.unamur.umatters.R;
import com.unamur.umatters.TagsSetupActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class RememberedLogin extends AsyncTask<String, String, String> {

    private Context context = null;

    public RememberedLogin(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String JsonResponse = null;
        String JsonDATA = params[1];
        System.out.println(params[1]);

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
        System.out.println(result);

        if (result == null) {
            Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
        } else {
            try {
                //Recuperation de la reponse de l'API sous forme de json
                JSONObject jsonObj = new JSONObject(result);
                boolean success = jsonObj.getBoolean("success");
                String message = jsonObj.getString("message");

                //Login succeed
                if (success) {

                    CurrentUser user = CurrentUser.getCurrentUser();
                    String email = user.getEmail();

                    SetCurrentUser setUser = new SetCurrentUser(context);
                    setUser.execute("http://mdl-std01.info.fundp.ac.be/api/v1/users/" + email);
                }
                //Login failed
                else {
                    Intent runApp = new Intent(context, LoginActivity.class);
                    context.startActivity(runApp);
                    ((Activity) context).finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
