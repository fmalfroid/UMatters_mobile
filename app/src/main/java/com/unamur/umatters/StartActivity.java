package com.unamur.umatters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.unamur.umatters.API.Login;
import com.unamur.umatters.API.RememberedLogin;

import org.json.JSONException;
import org.json.JSONObject;

public class StartActivity extends Activity {

    private static final String TAG = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        //Set the time before fading
        int SPLASH_TIME_OUT = 1500;

        SharedPreferences sharedPref = StartActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final String saved_email = sharedPref.getString(getString(R.string.saved_email_key), null);
        final String saved_password = sharedPref.getString(getString(R.string.saved_password_key), null);

        System.out.println(saved_email);
        System.out.println(saved_password);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "StartActivity: Launch screen");
                //Run the app

                if (saved_email != null && saved_password != null) {
                    login(saved_email, saved_password);
                } else {
                    Intent runApp = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(runApp);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }

    public void login(String email, String password) {
        JSONObject loginJson = new JSONObject();
        try {
            loginJson.put("email", email);
            loginJson.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CurrentUser user = CurrentUser.getCurrentUser();
        user.setEmail(email);
        user.setPassword(password);

        RememberedLogin login = new RememberedLogin(StartActivity.this);
        login.execute("http://mdl-std01.info.fundp.ac.be/api/v1/users/connect", String.valueOf(loginJson));
    }
}
